/*
 * creedengo - PHP language - Provides rules to reduce the environmental footprint of your PHP programs
 * Copyright © 2024 Green Code Initiative (https://green-code-initiative.org)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package fr.greencodeinitiative.php.checks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.ScriptTree;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.declaration.ClassDeclarationTree;
import org.sonar.plugins.php.api.tree.declaration.ClassMemberTree;
import org.sonar.plugins.php.api.tree.declaration.MethodDeclarationTree;
import org.sonar.plugins.php.api.tree.expression.FunctionCallTree;
import org.sonar.plugins.php.api.tree.statement.StatementTree;
import org.sonar.plugins.php.api.visitors.PHPSubscriptionCheck;
import org.sonarsource.analyzer.commons.annotations.DeprecatedRuleKey;

@Rule(key = "GCI22")
@DeprecatedRuleKey(repositoryKey = "gci-php", ruleKey = "D2")
public class UseOfMethodsForBasicOperations extends PHPSubscriptionCheck {

    protected static final String ERROR_MESSAGE = "Use of methods for basic operations";

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Collections.singletonList(Tree.Kind.FUNCTION_CALL);
    }

    @Override
    public void visitNode(Tree tree) {
        AtomicBoolean contains = new AtomicBoolean(false);

        final FunctionCallTree functionTree = ((FunctionCallTree) tree);
        final String functionName = functionTree.callee().toString();

        final List<Tree> parents = this.getAllParent(tree, new ArrayList<>());

        parents.forEach(parent -> {
            if (parent.is(Tree.Kind.SCRIPT)) {

                final ScriptTree specific = (ScriptTree) parent;
                final List<StatementTree> trees = specific.statements();

                trees.forEach(statement -> {

                    if (statement.is(Tree.Kind.CLASS_DECLARATION)) {

                        final List<ClassMemberTree> methodDeclarations = ((ClassDeclarationTree) statement).members()
                                .stream()
                                .filter(member -> member.is(Tree.Kind.METHOD_DECLARATION))
                                .map(MethodDeclarationTree.class::cast)
                                .filter(declarationTree -> this.isFunctionDeclared(declarationTree, functionName))
                                .collect(Collectors.toList());

                        if (methodDeclarations != null && !methodDeclarations.isEmpty()) {
                            contains.set(true);
                        }
                    }
                });
            }
        });

        if (!contains.get()) {
            context().newIssue(this, tree, ERROR_MESSAGE);
        }
    }

    public boolean isFunctionDeclared(final MethodDeclarationTree method, final String name) {
        if (method == null) {
            return false;
        }

        return method.name().text()
                .trim()
                .equals(name.trim());
    }

    public List<Tree> getAllParent(final Tree tree, final List<Tree> list) {
        if (tree == null)
            return list;

        final Tree parent = tree.getParent();

        if (parent == null)
            return list;

        list.add(parent);

        return this.getAllParent(parent, list);
    }
}

/*
 * ecoCode - PHP language - Provides rules to reduce the environmental footprint of your PHP programs
 * Copyright © 2023 Green Code Initiative (https://green-code-initiative.org)
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
package fr.greencodeinitiative.php;

import fr.greencodeinitiative.php.checks.*;
import org.sonar.api.SonarRuntime;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.plugins.php.api.visitors.PHPCustomRuleRepository;
import org.sonarsource.analyzer.commons.RuleMetadataLoader;

import java.util.List;

public class PhpRuleRepository implements RulesDefinition, PHPCustomRuleRepository {

    static final List<Class<?>> ANNOTATED_RULE_CLASSES = List.of(
            AvoidGettingSizeCollectionInLoopCheck.class,
            AvoidDoubleQuoteCheck.class,
            AvoidFullSQLRequestCheck.class,
            AvoidSQLRequestInLoopCheck.class,
            AvoidTryCatchWithFileOpenedCheck.class,
            AvoidUsingGlobalVariablesCheck.class,
            IncrementCheck.class,
            NoFunctionCallWhenDeclaringForLoop.class,
            UseOfMethodsForBasicOperations.class,
            AvoidMultipleIfElseStatementCheck.class
    );

    private static final String LANGUAGE = "php";
    private static final String NAME = "ecoCode";
    private static final String RESOURCE_BASE_PATH = "io/ecocode/rules/php";
    private static final String REPOSITORY_KEY = "ecocode-php";

    private final SonarRuntime sonarRuntime;

    public PhpRuleRepository(SonarRuntime sonarRuntime) {
        this.sonarRuntime = sonarRuntime;
    }

    @Override
    public void define(Context context) {
        NewRepository repository = context.createRepository(REPOSITORY_KEY, LANGUAGE).setName(NAME);
        RuleMetadataLoader ruleMetadataLoader = new RuleMetadataLoader(RESOURCE_BASE_PATH, sonarRuntime);
        ruleMetadataLoader.addRulesByAnnotatedClass(repository, checkClasses());
        repository.done();
    }

    @Override
    public String repositoryKey() {
        return REPOSITORY_KEY;
    }

    @Override
    public List<Class<?>> checkClasses() {
        return ANNOTATED_RULE_CLASSES;
    }
}

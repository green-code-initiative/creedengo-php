<?php

interface StatsContract
{
    // No method body: covers METHOD_DECLARATION without BLOCK.
    public function compute(array $values);
}

abstract class BaseStats
{
    // No method body: covers METHOD_DECLARATION without BLOCK in abstract class.
    abstract protected function normalize(array $values);
}

class AvoidMultipleIfElseStatementEdgeCases extends BaseStats implements StatsContract
{
    public function compute(array $values)
    {
        // Early return keeps fixture simple and avoids duplicating main conditional chains.
        if (empty($values)) {
            return 0;
        }

        $isReady = count($values) > 1;

        // Core complementary scenario for GCI2: non-binary IF condition.
        // No variable is collected from condition, ELSE traversal must remain safe (no NPE).
        if ($isReady) {
            return $values[0];
        } else {
            return -1;
        }
    }

    protected function normalize(array $values)
    {
        // Keep this method intentionally branch-free to avoid duplicating tested IF patterns.
        return $values;
    }
}



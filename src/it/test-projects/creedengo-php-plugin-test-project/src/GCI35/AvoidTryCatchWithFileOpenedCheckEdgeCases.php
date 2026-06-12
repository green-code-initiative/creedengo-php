<?php

class AvoidTryCatchWithFileOpenedCheckEdgeCases
{
    public function shouldCoverCompliantAndNonCompliantBranches(bool $flag): void
    {
        try {
            echo strtoupper('hello');
            echo 42;
        } catch (\Exception $e) {
            echo $e->getMessage();
        }

        try {
            $callable = 'fopen';
            echo $callable('myfile.txt', 'r');
        } catch (\Exception $e) {
            echo $e->getMessage();
        }

        try {
            fOpen('myfile.txt', 'r'); // NOK {{Avoid the use of try-catch with a file open in try block}}
        } catch (\Exception $e) {
            echo $e->getMessage();
        }

        try {
            // Function call not in the banned list: should stay compliant.
            parse_url('https://green-code-initiative.org');
        } catch (\Exception $e) {
            echo $e->getMessage();
        }

        try {
            // Assignment whose value is not a function call: should be ignored by the rule.
            $result = 1;
            echo $result;
        } catch (\Exception $e) {
            echo $e->getMessage();
        }

        try {
            if ($flag) {
                // RETURN_STATEMENT is intentionally not analyzed by GCI35 and should be ignored.
                return;
            }
        } catch (\Exception $e) {
            echo $e->getMessage();
        }
    }
}



<?php

function foo()
{
    $counter = 0;
    $counter++; // NOK {{Remove the usage of $i++. prefer ++$i}}
    return $counter;
}

function bar()
{
    $counter = 0;
    return ++$counter; // Compliant
}

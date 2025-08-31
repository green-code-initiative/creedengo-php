<?php

$a = 1;
$b = 2;
function somme()
{
    $GLOBALS['b'] = $GLOBALS['a'] + $GLOBALS['b'];
} // NOK {{Prefer local variables to globals}}

function somme2()
{
    global $a, $b;
    $c = $a + $b;
} // NOK {{Prefer local variables to globals}}

function somme3($c, $d) // Compliant
{
    $e = $c + $d;
    return ++$e;
}

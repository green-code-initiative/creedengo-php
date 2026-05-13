<?php

$a = 1;
$b = 2;
function somme() // NOK {{Prefer local variables to globals}}
{
    $GLOBALS['b'] = $GLOBALS['a'] + $GLOBALS['b'];
}

function somme2() // NOK {{Prefer local variables to globals}}
{
    global $a, $b;
    $c = $a + $b;
    return $c;
}

function somme3($c, $d) // Compliant
{
    $e = $c + $d;
    return ++$e;
}

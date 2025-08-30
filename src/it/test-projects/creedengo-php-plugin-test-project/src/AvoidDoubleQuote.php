<?php

$lastName = 'Hugo';
$name = "Hamon"; // NOK {{Avoid using double quote ("), prefer using simple quote (')}}
$age = 19;
$isStudent = true;
$cours = array('physique','chimie','informatique','philosophie');

mc = new MyClass();

mc->display($lastName);
mc->display($name);
mc->display('<br/>');
mc->display("<br/>"); // NOK {{Avoid using double quote ("), prefer using simple quote (')}}
mc->display($age);

$identite = $lastName .' '. $name;
mc->display($identite);

mc->myFunction($name, $age, $isStudent);

mc->myFunction("name", "age", "isStudent"); // NOK {{Avoid using double quote ("), prefer using simple quote (')}}

mc->myFunction('name', 'age', 'isStudent');

mc->myFunction("name", 'age', "isStudent"); // NOK {{Avoid using double quote ("), prefer using simple quote (')}}


class MyClass
{
    public function myFunction($name, $age, $isStudent)
    {
        if ($name == null) {
            return 'toto' + $age;
        } else {
            return 'tata' + $isStudent;
        }
    }

    public function display($msg)
    {
        if ($msg != null) {
            return $msg;
        } else {
            return '';
        }
    }
}
<?php

try {
    $file = 'file';
    PDF_open_image_file(
        pdf_new(),
        'jpeg',
        $file,
        '',
        0
    ); // NOK {{Avoid the use of try-catch with a file open in try block}}
} catch (Exception $e) {
    echo "Error opening $file : " . $e->getMessage();
}

try {
    $file = 'file';
    try {
        echo 'Hello';
    } catch (Exception $e) {
        PDF_open_image_file(
            pdf_new(),
            'jpeg',
            $file,
            '',
            0
        ); // NOK {{Avoid the use of try-catch with a file open in try block}}
    }
} catch (Exception $e) {
    echo "Error opening $file : " . $e->getMessage();
}

try {
    $file = 'file';
    $picture = readfile('toto.gif'); // NOK {{Avoid the use of try-catch with a file open in try block}}
} catch (Exception $e) {
    echo 'Error opening $file : ' . $e->getMessage();
}

try {
    $file = 'file';
    if ($file) {
        $picture = fopen('myfile.txt', 'r'); // NOK {{Avoid the use of try-catch with a file open in try block}}
    }
} catch (Exception $e) {
    echo 'Error opening $file : ' . $e->getMessage();
}

try {
    $file = 'file';
    if ($file) {
        echo 'Hello';
    } else {
        $picture = fopen('myfile.txt', 'r'); // NOK {{Avoid the use of try-catch with a file open in try block}}
    }
} catch (Exception $e) {
    echo 'Error opening $file : ' . $e->getMessage();
}

try {
    $file = 'file';
    switch ($file) {
        case 'hello';
            $picture = fopen('myfile.txt', 'r'); // NOK {{Avoid the use of try-catch with a file open in try block}}
            break;
        case 'hello2';
            echo 'Hello BIS';
            break;
        default:
            echo 'Hello BIS';
    }
} catch (Exception $e) {
    echo 'Error opening $file : ' . $e->getMessage();
}

try {
    $file = 'file';
    switch ($file) {
        case 'hello';
            echo 'Hello';
            break;
        case 'hello2';
            echo 'Hello BIS';
            break;
        default:
            $picture = fopen('myfile.txt', 'r'); // NOK {{Avoid the use of try-catch with a file open in try block}}
    }
} catch (Exception $e) {
    echo 'Error opening $file : ' . $e->getMessage();
}

try {
    $file = 'file';
    for ($i = 0; $i < 10; $i++) {
        foreach ($a as $b) {
            $picture = fopen('myfile.txt', 'r'); // NOK {{Avoid the use of try-catch with a file open in try block}}
        }
    }
} catch (Exception $e) {
    echo 'Error opening $file : ' . $e->getMessage();
}

try {
    throw new SpecificException('Hello');
} catch (SpecificException $e) {
    echo $e->getMessage() . ' catch in\n';
} finally {
    echo $e->getMessage() . ' finally \n';
}

try {
    throw new \Exception('Hello');
} catch (\Exception $e) {
    echo $e->getMessage() . ' catch in\n';
}

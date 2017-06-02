<?php

  //this file is to create a new project

  //connection string goes here

  $conn = new mysqli('localhost','root','', 'dione');



  $project_name      =  $_POST['project_name'];
  $parent_folder_id =  $_POST['parent_folder_id'];
  $this_project_id   =  "fld".rand(0,99999);
  $files = $_FILES['files']['name'];
  $file_names = "";
  $file_path = "";
  $root = $_SERVER['DOCUMENT_ROOT']."/sita";
  $time_stamp = date("mdYhis");
  ;
  $dir_name = str_replace(' ', '', $project_name).$time_stamp;
  mkdir("$root"."/uploads/".$dir_name,0755,true);
  $file_upload_root = $root."/uploads/".$dir_name."/";



  //upload selected files to server
  for ($i=0; $i < count($files); $i++) {
    $tmpFilePath = $_FILES['files']['tmp_name'][$i];

    //Make sure we have a filepath
    if ($tmpFilePath != ""){
      //Setup our new file path
      $file_path = $file_upload_root . $_FILES['files']['name'][$i];
      //Upload the file into the temp dir
      if(move_uploaded_file($tmpFilePath, $file_path)) {
        $file_names = $file_names.$_FILES['files']['name'][$i].",";
      }
      else{
        echo "move failed";
      }
    }
    else{
      echo "no temp file";
    }
  }

  //get the json outputs for the files now


  //add an entry for the created project
  if($conn->query("INSERT INTO projects(id, folder_id, project_name) VALUES('$this_project_id','$parent_folder_id','$project_name')")){
    //save the uploaded file names
    if($conn->query("INSERT INTO projectfiles(id,file_list) VALUES('$this_project_id','$file_names')")){
      // echo "project created";

      $file_path = $file_upload_root;
      $arg1 = "-setRepository ".$file_path;
      $arg2 = "-addProject ".$file_path;
      $arg3 = "-parse ".$file_path;

      $args = "\"".$arg1."\""." \"".$arg2."\""." \"".$arg3."\"" ;
      $path = $root."/apis/dione/prest-light.jar";
      $jsonpath = "prest-console.lib.java-json.jar";

      $command = "java -jar ".$path." ".$args;
      exec('unset DYLD_LIBRARY_PATH ;');
      putenv('DYLD_LIBRARY_PATH');
      putenv('DYLD_LIBRARY_PATH=/usr/bin');
      exec($command." 2>&1", $output);


      $mode_package   = $output[0];
      $mode_file      = $output[1];
      $mode_class     = $output[2];
      $mode_method    = $output[3];

      //save the result jsons
      if($conn->query("INSERT INTO project(id,class_data,package_data,file_data,method_data) VALUES('$this_project_id','$mode_class','$mode_package','$mode_file','$mode_method')")){
        // print_r($mode_class);
        echo $this_project_id;
      }
      else{
        echo "error creating a project";
      }
    }
    else{
      echo "error in saving file names";
    }
  }
  else{
    echo mysqli_error($conn);
  }
?>

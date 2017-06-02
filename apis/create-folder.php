<?php

  //this file is to create a new folder

  //connection string goes here

  $conn = new mysqli('localhost','root','', 'dione');



  $folder_name      =  $_POST['folder_name'];
  $parent_folder_id =  $_POST['parent-folder'];
  $this_folder_id   =  "fld".rand(0,99999);

  if($conn->query("INSERT INTO folder(id, parent_folder_id, folder_name) VALUES('$this_folder_id','$parent_folder_id','$folder_name')")){
    echo $this_folder_id;
  }
  else{
    echo mysqli_error($conn);
  }
?>

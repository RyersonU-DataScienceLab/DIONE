var upload_files_form = document.getElementById("uploadFiles");

upload_files_form.addEventListener("submit",function(event){
  event.preventDefault();
  var data = new FormData();
  var chosen = document.getElementById("files");
  for (var i =0 ; i < chosen.files.length ; i++){
      data.append("files[]",chosen.files[i]);
      console.log(i+' files uploaded');
  }
  console.log(data);
  var request = new XMLHttpRequest();

  //write a Java file to handle the file upload and do all your work and return the 4 JSON files from the JAVA file.

  request.open("post"," "); //add the java file name here in the quotes 
  request.send(data);
  request.onreadystatechange = function(){
     if(request.readyState == XMLHttpRequest.DONE){
        console.log(request.responseText);
     }
  }
})

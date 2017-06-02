//listen to create project button to list all the folders
var new_project = document.querySelector("form#new-project");

new_project.addEventListener("submit",function(event){
  event.preventDefault();
  var parent_folder_id = document.querySelector("#folder_value_for_new_project");
  parent_folder_id = parent_folder_id.getAttribute("data-value");
  var data = new FormData(this);
  data.set("parent_folder_id",parent_folder_id);
  var create_project_request = new XMLHttpRequest;
  create_project_request.open("POST","apis/create-project.php");
  create_project_request.send(data);
  create_project_request.onreadystatechange = function(){
    if(create_project_request.readyState == XMLHttpRequest.DONE){
      console.log(create_project_request.responseText);
    }
  }
})

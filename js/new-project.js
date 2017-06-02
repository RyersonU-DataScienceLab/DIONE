//listen to create project button to list all the folders
var new_project = document.querySelector("form#new-project");

new_project.addEventListener("submit",function(event){
  event.preventDefault();
  var parent_folder_id = document.querySelector("#folder_value_for_new_project");
  parent_folder_id = parent_folder_id.getAttribute("data-value");
  var data = new FormData(this);
  data.set("parent_folder_id",parent_folder_id);
  var project_text = data.get("project_name");
  var create_project_request = new XMLHttpRequest;
  create_project_request.open("POST","apis/create-project.php");
  create_project_request.send(data);
  create_project_request.onreadystatechange = function(){
    if(create_project_request.readyState == XMLHttpRequest.DONE){
      var project_id = create_project_request.responseText;
      document.querySelector("#create-project").checked = false;

      //create a project and append it to the tree
      var project = document.createElement("div");
      project.setAttribute("class","project");
      project.setAttribute("id",project_id);
      project.setAttribute("onclick","get_project('"+project_id+"')");

      var project_name = document.createTextNode(project_text);
      project.appendChild(project_name);


      // appending to the container
      console.log(parent_folder_id);
      var container = document.getElementById("f_"+parent_folder_id);
      container = container.getElementsByClassName("folder-container")[0];
      container.appendChild(project);

      get_project(project_id);
    }
  }
})

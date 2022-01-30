$(document).ready(function () {
  $("#csv").change(function () {
    $("#selectedFile").text(
      $("#csv")
        .val()
        .replace(/C:\\fakepath\\/i, "")
    );
  });

  $("#btnSubmit").click(function (event) {
    //validate input fields
    if ($("#csv").val().length > 1) {
      var filename = $("#csv").val();

      // Use a regular expression to trim everything before final dot
      var extension = filename.replace(/^.*\./, "");

      // If there is no dot anywhere in filename, we would have extension == filename
      if (extension == filename) {
        extension = "";
      } else {
        // if there is an extension, we convert to lower case
        // (N.B. this conversion will not effect the value of the extension
        // on the file upload.)
        extension = extension.toLowerCase();
      }

      if (extension != "csv") {
        alert("Only csv formats are allowed!");
        event.preventDefault();
        return;
      }

      //stop submit the form, we will post it manually.
      event.preventDefault();

      // Get form
      var form = $("#fileUploadForm")[0];

      // Create an FormData object
      var data = new FormData(form);
      // data.append("destPath", $("#destPath").val());

      // disabled the submit button
      $("#btnSubmit").prop("disabled", true);

      $(".loading").removeClass("loading--hide").addClass("loading--show");
      $(".result label").hide();
      $.ajax({
        type: "post",
        url: "/bin/import-file",
        data: data,
        processData: false,
        // binary content
        contentType: "text/csv",
        cache: false,
        success: function (data) {
          console.log(data);

          
          // $(".result label").text(JSON.stringify(data));
          // $(".result label").show();
          // $(".loading").removeClass("loading--show").addClass("loading--hide");
          // $("#btnSubmit").prop("disabled", false);
        },
        error: function (e) {
          $(".result label").text(e.responseText);
          $(".result label").show();
          $(".loading").removeClass("loading--show").addClass("loading--hide");
          $("#btnSubmit").prop("disabled", false);
        },
      });
    } else {
      alert("Please, fill the mandatory fields");
      // Cancel the form submission
      event.preventDefault();
      return;
    }
  });

  $("#btnUpdate").click(async function (event) {
    event.preventDefault();
    // var checkUploadedArticles = await setInterval(checkUpdatedArticles, 1000);

    await $.ajax({
      url: "/api/assets/ucs-exercise-heritier.json",
      type: "post",
      contentType: "application/json",
      success: function (data) {
        if (data.fileStatus) {
          console.log("data");

          $(".result label").text(`${data.created} out of ${data.total}`);
          $(".result label").show();
           
          $("#btnUpdate").prop("disabled", false);
          $("#csv").prop("disabled", false);
        } else if (data.noChange) {
          $(".result label").text("No changes detected in the file");
          $(".result label").show();
        } else {
          $(".result label").text("Invalid file");
          $(".result label").show();
        }
      },
      error: function (e) {
        
        $(".result label").text(e.responseText);
        $(".result label").show();
        $(".loading").removeClass("loading--show").addClass("loading--hide");
        $("#btnSubmit").prop("disabled", false);
      },
    });


    var checkUpdatedArticles = function a() {
      $.ajax({
        url: "/api/assets/ucs-exercise-heritier.json",
        type: "get",
        contentType: "application/json",
        success: function (data) {
          console.log(data);

          // if(data.created == data.total || data.noChange || !data.fileStatus ) clearInterval(checkUploadedArticles);
          if (data.fileStatus) {
            $(".result label").text(`${data.created} out of ${data.total}`);
            $(".result label").show(); 
            $("#btnUpdate").prop("disabled", false);
            $("#csv").prop("disabled", false);
          } else if(data.noChange) {
            $(".result label").text("No changes detected in the file");
            $(".result label").show();
          }else{
            $(".result label").text("Invalid file" );
            $(".result label").show();
          }

        },
        error: function (e) {
          console.log(e);

          $(".result label").text(e.responseText);
          $(".result label").show();
          $(".loading").removeClass("loading--show").addClass("loading--hide");
          $("#btnSubmit").prop("disabled", false);
        },
      });
    }
  });
});

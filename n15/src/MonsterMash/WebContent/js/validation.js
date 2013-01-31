/**
 * A generic javascript validator, which uses html elements such as
 * a data-set or classes.
 * 
 * Author: Sam Sherar <sbs1@aber.ac.uk>
 * 
 * Class selectors:
 *   - required
 *   		Make sure that the field not empty
 *   - numeric
 *   		Tests if it's numeric
 *   - password_confirm
 *   		Expects two password fields, and checks if they are same
 *   - email
 *   		Checks if the value is a valid email
 * Dataset selectors
 * 	 - minlength
 * 			Checks if the length is not less than the value given
 * 
 * Example usage:
 * <form type="post" onsubmit="return validate(this);">
 * <input type="text" name="foo" data-minlength="3" class="required" />
 * <input type="submot" />
 * 
 * You can also call the submittion from a link by just passing the form value
 * into it.
 * 
 * <a href="javascript:validate(document.forms[0]);">Submit</a>
 * 
 */
function validate(formVar) {
	var mesg = "";
	var password = "";
	for(i = 0; i < formVar.elements.length; i++) {
		ele = formVar.elements[i];
		if(ele.className != "") {
			var classes = ele.className.split(" ");

			for(j = 0; j < classes.length; j++) {
				if(classes[j] == "required") {
					if(ele.value == null || ele.value == "") {
						mesg += ele.name + " is empty! \n";
					}
				} 
				if(classes[j] == "numeric") {
					if(isNaN(ele.value)) {
						mesg += ele.name + " is not numeric \n";
					}
				}
				if(classes[j] == "password_confirm") {
					if(password == "") {
						password = ele.value;
					} else {
						if(password != ele.value) {
							mesg += "passwords are not the same \n";
						}
					}
				}
					var re = new RegExp("[a-zA-Z0-9-._]+@[a-zA-Z0-9-.]+[a-zA-Z]+");
					if(!ele.value.match(re)) {
						mesg += "you have not entered a valid email\n";
					}
				}
			}
		}
		if(ele.dataset.minlength != undefined) {
			if(ele.value.length < parseInt(ele.dataset.minlength)) {
				mesg += ele.name + " is too short! Minimum: " + ele.dataset.minlength + " \n";
			}
		}
	}
	if(mesg != "") {
		alert(mesg);
		return false;
	} else {
		formVar.submit();
		return true;
	}
}
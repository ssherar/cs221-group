cs221-group
===========

Monster Mash group project

Setting up Github on central
----------------------------

If you're using the IS terminals in the Delph/B23 or the solarium, sadly they don't have GUI for git. However they have the commandline toolkit installed on Central

First load up PuTTY (search in the start bar, or it's somwhere in All Programs->Course->Computing) and double click on 'Central'. From there login as you would normally. Also when it asks for a terminal just hit enter. In the end you should have something like `central:~ $'

First thing we need to do is assign a name and email to your git account on the local machine. Enter:

	git config --global user.name "John Doe"
	git config --global user.email "john@example.com"
	
*Please* remember to enter your email address which is *the same* as the one you registered with github.

Next thing we need to do is create a key for the pushing code to github.

	#change the directord
	cd ~/.ssh
	#Generate the key
	ssh-keygen -t rsa -C "email@address.here"

It will ask you for a password. For the sake of easyness, just enter the password you created for the github account. This will create a hash to verify your login to github.

Next go onto windows and open up notepad. goto M:\.ssh\id_rsa.pub and copy everything there. *Make sure there are no trailing whitespaces!*

Login to github web interface and go to admin and then SSH keys, create a new key with the name 'central' while pasting everything in your clipboard in the big box. Then press save.

To test if this works type into central:
	ssh -T git@github.com

type "yes" and enter your password.

If it comes up with `Hi username! You've successfully authenticated, but GitHub does not provide shell access.` you're good to go.

To pull the repositry down, go whereever you want to put it in your filestore, and type
	git pull git@github.com:ssherar/cs221-group.git

To add files type
	git add path/to/file.txt

To commit those files to the local repo
	git commit -m "Meaningful message"

To push to the server
	git push origin master

and its _that_ easy!
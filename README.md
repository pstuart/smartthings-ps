smartthings
===========

Place to hold my files and revisions on smartthings apps and devices


How to set up a custom device



You need IDE access. https://graph.api.smartthings.com/

go into my device types https://graph.api.smartthings.com/ide/devices

click + New SmartDevice

fill out just name, namespace and author and click create

copy and paste my code from the github https://github.com/pstuart/smartthings/blob/master/generic_camera.groovy

Click the save button

Click the publish button, for me

Go to My Devices

click + Add New Devices

Give it the following:
Name = anything you want
device Network Id = unique id (should be the hex IP and hex port of the device but my code will auto insert that when you set the ip and take a photo)
Type = Generic Camera Device
Version = Published
Location = your location
hub = your hub
group = what folder do you want the "thing" in, none is the default.

then click create

you should then get a page that shows you the device info

look for a preferences section with a (edit) link.

Click the edit link, add the properties for the camera: (these are one of my camera's settings, replace with your values)

Camera IP Address: 192.168.101.249
Camera Port: 80
Camera Path to Image: /SnapshotJPEG?Resolution=640x480&Quality=Clarity
Does Camera require User Auth?: true
Does Camera use a Post or Get, normally Get?: GET
Camera User: username
Camera Password: password

click save.

Go to your app and under things you should now see a new camera thing. Tap it.

Tap the take button, should see a picture show up if everything worked.

If not, go back into the IDE https://graph.api.smartthings.com/

Go to the Logs section https://graph.api.smartthings.com/ide/logs

go back to your app and device tap the take button again and paste in the console results for the camera.

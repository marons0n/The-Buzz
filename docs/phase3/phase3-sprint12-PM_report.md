# Phase 3 Sprint 12 - PM Report Template

## Team Information [10 points total]

### Team Information:

* Number: 23
* Name: Team 23
* Mentor: <Maya Itty, mni226@lehigh.edu>
* Weekly live & synchronous meeting:
    * 11/26 at 9pm

### Team Roles:

* Project Manger: <nStefania, sad823@lehigh.edu>
    * Has this changed from last week (if so, why)?
    * No, same sprint as last week
* Backend developer: <Andre Escobedo, aee225@lehigh.edu>
* Admin developer: <Matthew Aronson, maa362@lehigh.edu>
* Web developer: <Lily Fandre, lmf226@lehigh.edu>
* Mobile developer: <Waren Noubi, wdn225@lehigh.edu>

### Essential links for this project:

* Team's Dokku URL(s)
    * https://team-untitled-23.dokku.cse.lehigh.edu/
* Team's software repo (bitbucket)
    * https://bitbucket.org/sml3/cse216_fa24_team_23
* Team's Jira board
    * https://cse216-fa24-team-23.atlassian.net


## General questions

1. Did the PM for this week submit this report (If not, why not?)? 
    * Yes
2. Has the team been gathering for a weekly, in-person meeting(s)? If not, why not?
    * Yes, we met after recitation on last Tuesday to prepare for the upcoming week because some of us would be out of town throughout the week/this Tuesday. 

3. Summarize how well the team met the requirements of this sprint.
    * The requirements were met and each member worked on their requirements mostly individually, with check-ins over Slack. 

4. Report on each member's progress (sprint and phase activity completion) – "what is the status?"
    * The backend may have some technical debt but all of the components are working so far. Everyone elses status is also complete and working, with some potential technical debt that we will work on eliminating for the next sprint. 
    * I checked in about work and progress over slack by asking team members for updates on their work and estimates of when they would be ready. Other team members also communicated to find out when working code would be pushed to the bitbucked. 
    * The frontend developer was able to implement files and image attachments fairly easily, while caching was more of a challenge. Both were completed in the end. 

5. Summary of "code review" during which each team member discussed and showed their progress – "how did you confirm the status?"
    * We checked in around daily over slack to see if members were making progress and if there were any roadblocks. We did not meet in person due to travel, sports, and being out of town. Slack was sufficient this week because we met synchronously to catch everyone up on Tuesday, and checked in regularly over slack. We saw that everyones code worked due to components functioning together and that all requirements were met, and we used unit tests to confirm that code was working and could be integrated. 

6. What did you do to encourage the team to be working on phase activities "sooner rather than later"?
    * Checked in on progress regularly and the whole team was communicating to be transparent, which helped people stay on top of the work. 

7. What did you do to encourage the team to help one another?
    * Regular communication over slack made it easy for people to reach out to one another if they needed help with components. Additionally, meeting on last Tuesday adter recitation helped us get an idea of who we needed to collaborate with in the coming week and what everyone could help each other with. 

8. How well is the team communicating?
    * We are communicating fairly well. Our main difficulty is aligning schedules to be able to meet in person or over zoom, since many of the members have prior commitments often, and this week multiple members were travelling. However, we send lots of slack messages when we need to meet and generally end up finding a time. Most of the slack or zoom check-ins are business, but when we work together in person we are getting to know each other. When a team member is not responding, we fix it by pinging them directly or texting them by their phone number in case they are not getting slack notifications, and this generally resolves the issue. Communication has improves as the semester has gone on and we have needed to be in contact more. 

9. Discuss expectations the team has set for one another, if any. Please highlight any changes from last week.
    * Our expectations are to be communicative and get our work done, and to ask for help when needed. Especially when we are checking in remotely so people need to be responsive even when there's not always the opportunity to have a set time to get together and work. There have not been changes from last week. 

10. If anything was especially challenging or unclear, please make sure this is [1] itemized, [2] briefly described, [3] its status reported (resolved or unresolved), and [4] includes critical steps taken to find resolution.
    * 1. Challenge: Error with CORS
        * Status: Resolved
        * Description: Error with CORS that prevented work at first - error with oAuth in backend
        * Critical steps taken to find resolution: Communicated with backend developer and looked through backend and frontend code
    * 2. Challenge: Uploading files not working
        * Status: Not resolved 
        * Description: Uploading files does not connect them to the google drive, can't upload posts or comments with files attached
        * Steps taken: Lots of debugging. Will continue this week to have it functioning by next Tuesday. Will visit office hours if issues can't be resolved
    * 3. Challenge: Caching
        * Status: Not resolved
        * Description: Caching wasn't able to be implemented due to other aspects not working
        * Steps taken: This was less of a focus this week due to issues with the upload functionality, but will be worked on and remedied by the end of sprint 13

11. What might you suggest the team or the next PM "start", "stop", or "continue" doing in the next sprint?
    * Make sure teammates report on their challenges so we are more precisely aware of the roadblocks and are able to help each other sooner. 

### Back-end

1. 
    * Quality of back-end code is good and has comments. Code review process was conducted by discussing the code during a zoom meeting and looking over it to see if it has any merge conflicts with previous pushes, and if code was clear, but backend doesn't work with some other aspects of the project so it is not being connected with yet. 
    * Biggest issue: 
    * A remote frontend was created for simplicity. Adding files does not work currently and doesnt get added to the google drive, so that will be worked on over the next week. That was the biggest issue this week. 
    * Units tests for cloud storage and service account were created and were helpful in determining what is missing. 
    * Appropriate dependencies present in the pom.xml. 
    * Unit tests are adequate, more could be added to be more robust and assess all functionalities and fringe cases. 

### Admin

1. 
    * Overall process was good, Jira board was used adequately, all functionality works and technical debt from previous sprints was eliminated. 
    * All necessary tables and sequel statements are present. Tables can be altered, deleted, etc. 
    * Menu was updated to have clearer options
    * Some functionality can't be tested due to the database not being updated
    * Users and ideas can be invalidated
    * Pom.xml file updated

### Web

1. 
    * Overall process was good with some issues. Jira was used appropriately, tasks were clearly defined though not all have been completed. 
    * Issues:
    * Connecting to backend
    * Had issue with CORS/oAuth, which was a very time intensive issue. Worked hard to fix it
    * Post, commenting, etc was working. However with links to files, those do not work due to them not working in the backend. 
    * Added user, token, and oAuth status in the bottom right for clarity. 
    * Attaching links/files works but posting them doesn't work due to problems with backend
    * All other functionality like posting, comments, upvotes/downvotes work as before other than attaching files
    * Caching has not been implemented yet due to difficulty with the other aspects but that will be implemented as soon as other parts are sorted out. 
    * Used a mock backend to make everything function. 
    * Dependencies appropriately updated
    * Organization of files/packages is clear 
    * Technical debt: Making the box in the lower right that describes the user and session. Needs to be eliminated later. Also, the mock backend can't be used later so needs to be aligned with the real backend. 

### Mobile
1. 
    * Progress:
    * Upload pictures, files to posts and comments
    * Added folder for file handling
    * Adding picture from device camera works or from gallery
    * Testing: Testing has been added but is still in progress to cover all functionality
    * Pull requests were made
    * Issues: Connection with backend not working to upload to database
    * Biggest issue was not seeing full functionality because of problems with the backend. 
    * Dependencies are all correct and updated
    * All of the files and packages are organized
    * Only technical debt is that more tests would be better to ensure all functionality works


### Project Management
1. 
    * Meeting with our mentor more would have been helpful, as we only met with Maya on last Tuesday to help us get started on this weeks sprint. It would have been good to check in another time to get any questions that came up throughout the week answered. 
    * Jira had adequate detail, had some broader tasks and some more specific ones like specific functionalities of the different team members. Some more detail would have been better but I didn't know the exact tasks that all team members were done with or still working on at times. 
    * Team meetings were held on zoom, with everyone asked to describe thier progress and what they were still struggling on or what they struggled on, and how they resolved it. 
    * We mostly used Jira to keep track of assignments, as well as near-daily scrums where we reported out progress and time estimates of when components would be done or ready to push. 
    * An issue that arose was difficulty aligning everyones schedules, as our meetings had to be fully remote this week and this made it a bit harder to keep track of everyone and a bit harder to collaborate. 
    * Most significant obstacle was finding a time to meet, and getting the backend functional due to the difficulty of the backend. This has generally been an obstacle over the past sprints so it is likely just the difficulty of all of the functionality, and not the fault of any teammate. To do better with the coding parts, we could have utilized office hours more.
    * Our time estimates were fairly accurate - knowing that frontend and backend would take a good amount of time, as we've had many sprints already and were able to predict the intensity of different aspects. 
    * Main causes for concern for a customer would be that files added to comments or ideas are not stored and don't allow a post to be made. Additionally, caching is not implemented so performance can be slow if this was being used with a lot of users and data.













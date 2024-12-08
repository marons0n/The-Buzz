# Phase 3' Sprint 13 - PM Report Template
Use this form to provide your project manager report for Phase 3' (Prime).

## Team Information [10 points total]

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


## Beginning of Phase 3' [20 points total]
Report out on the Phase 3 backlog and any technical debt accrued during Phase 3.

1. What required Phase 3 functionality was not implemented and why? 
    * These high-priority items are to be added to the Phase 3' backlog that appears on your Jira board.
    * This should be a list of items from the Phase 3 backlog that were not "checked off".
    * List this based on a component-by-component basis, as appropriate.
        * Admin-cli
            1. Missing functionality 1: Google API/Drive Functionality
                * Why: Problems with it in backend
            2. Missing functionality 2: Prepopulating data with test data
                * Why: Unsure
            3. Missing functionality 2: List documents
                * Why: Hadn't had time to implement that last week
        * Backend
            1. Missing functionality 1: Storing photos/files uploaded in google drive
                * Why: Unsure, debugging wasn't working
        * Mobile FE
            1. Missing functionality 1: Uploading photos, connect them to database
                * Why: Issues with backend
            2. Missing functionality 2: Figure out how to connect to backend
                * Why: Unable to find the issue in the code
        * Web FE
            1. Missing functionality 1: Google drive files stored
                * Why: Weren't able to find the issues
            2. Missing functionality 2: Caching
                * Why: Didn't have time to get to it
            3. Missing functionality 3: Adding links to ideas and comments
                * Why: Weren't able to find the issues

2. What technical debt did the team accrue during Phase 3?
    <!-- PM: When editing this template for your submission, you may remove this guidance -->
    * These items are to be added to the Phase 3' backlog that appears on your Jira board.
    * List this based on a component-by-component basis, as appropriate.
        * Admin-cli
            1. Tech debt item 1: Code a bit hard to read 
        * Backend
            1. None
        * Mobile FE
            1. None
        * Web FE
            1. None


## End of Phase 3' [20 points total]
Report out on the Phase 3' backlog as it stands at the conclusion of Phase 3'.

1. What required Phase 3 functionality still has not been implemented or is not operating properly and why?
    * These high-priority items are to be added to the Phase 3' backlog that appears on your Jira board.
    * This should be a list of items from the Phase 3 backlog that were not "checked off".
    * List this based on a component-by-component basis, as appropriate.
        * Admin-cli: None
        * Backend: None
        * Mobile FE
            1. Missing functionality 1: Opening photos from drive
                * Why: Logging in issues
        * Web FE
            1. Missing functionality 1: Displaying photos uploaded (shows a link to them instead)
                * Why: Haven't been able to get that functionality implemented. 
            2. Missing functionality 2: Caching
                * Why: Unsure if it's working because not showing in console logs

2. What technical debt remains?
    * List this based on a component-by-component basis, as appropriate.
        * Admin-CLI: None
        * Web FE: Photos show as link instead of as photo, caching doesn't show in console logs. Purple background doesn't show on some pages of the site.
        * Mobile: Issues finding photos in the mobile device folders 

3. If there was any remaining Phase 3 functionality that needed to be implemented in Phase 3', what did the PM do to assist in the effort of getting this functionality implemented and operating properly?
    * Scheduled meetings and coordinated between members

4. Describe how the team worked together in Phase 3'. Were all members engaged? Was the work started early in the week or was there significant procrastination?
    * Members worked well together, there were some delays not due to procrastination but due to a team member who had a major component getting sick, so that delayed the group a bit, but other members worked to help everything get finished and functioning. 

5. What might you suggest the team or the next PM "start", "stop", or "continue" doing in the next Phase (if there were one)?
    * Something our team had issues with during this phase was having regular meetings, so for a future PM I would recommend to start scheduling weekly meetings on the same day when everyone was definitely available. Due to how many different schedules and committments our teammates had, it was difficult to have consistent meetings.
    * Another thing that would be beneficial to do more of would be to have more in-person meetings so people could look at each others code more effectively, or use one of the code/screen sharing programs people demo'd in class. 

## Role reporting [50 points total]
Report-out on each team members' activity, from the PM perspective (you may seek input where appropriate, but this is primarily a PM related activity).
**In general, when answering the below you should highlight any changes from last week.**

### Back-end
What did the back-end developer do during Phase 3'?
1. Overall evaluation of back-end development (how was the process? was Jira used appropriately? how were tasks created? how was completion of tasks verified?)
    * The back-end developer completed all of the tasks and appropriately updated everyone on his progress. Completion was verified by the functionality of his code and unit tests. 
2. List your back-end's REST API endpoints
    * GET /auth/google, GET /auth/google/callback, POST /auth/logout, GET /api/ideas, POST /api/ideas, PUT /api/ideas/:id/vote, GET /api/ideas/:id/comments, POST /api/ideas/:id/comments, PUT /api/ideas/:id/comments/:commentId, GET /api/profile, PUT /api/profile, GET /api/profile/:userId
3. Assess the quality of the back-end code
    * Everything is functioning and clear, so the quality is good
4. Describe the code review process you employed for the back-end
    * Code review was mostly done by Lily, who was backend in the previous phase, and she helped fix problems with it. 
5. What was the biggest issue that came up in code review of the back-end server?
    * No issues
6. Is the back-end code appropriately organized into files / classes / packages?
    * Yes
7. Are the dependencies in the `pom.xml` file appropriate? Were there any unexpected dependencies added to the program?
    * Yes, no
8. Evaluate the quality of the unit tests for the back-end
    * Adequate and functioning
9. Describe any technical debt you see in the back-end
    * None 

### Admin
What did the admin front-end developer do during Phase 3'?
1. Overall evaluation of admin app development (how was the process? was Jira used appropriately? how were tasks created? how was completion of tasks verified?)
    * Good, all of the unfinished tasks were wrapped up and all functionality was implemented. Task progress was appropriately communicated to the rest of the group. 
2. Describe the tables created by the admin app
    * The tables are votes, users, ideas, and comments. Ideas and comments tables had the added columns link and file, for the links and files put into the ideas and votes. 
3. Assess the quality of the admin code
    * Good. The last week was used for fixing any missing functionality and cleaning up code, so the readability is good. 
4. Describe the code review process you employed for the admin app
    * The code was clear and functioning
5. What was the biggest issue that came up in code review of the admin app?
    * None
6. Is the admin app code appropriately organized into files / classes / packages?
    * Yes
7. Are the dependencies in the `pom.xml` file appropriate? Were there any unexpected dependencies added to the program?
    * Yes, no
8. Evaluate the quality of the unit tests for the admin app
    * Adequate
9. Describe any technical debt you see in the admin app
    * None

### Web
What did the web front-end developer do during Phase 3'?
1. Overall evaluation of Web development (how was the process? was Jira used appropriately? how were tasks created? how was completion of tasks verified?)
    * Web development process was good this week with almost all functionality implemented and the developer even helped with getting back-end working. 
2. Describe the different models and other templates used to provide the web front-end's user interface
    * Nothing new this week
3. Assess the quality of the Web front-end code
    * Good, organized
4. Describe the code review process you employed for the Web front-end
    * The backend and frontend worked together to review the code, and since I haven't been either, I trusted their assessments of the quality. 
5. What was the biggest issue that came up in code review of the Web front-end?
    * None
6. Is the Web front-end code appropriately organized into files / classes / packages?
    * Yes
7. Are the dependencies in the `package.json` file appropriate? Were there any unexpected dependencies added to the program?
    * Yes, no
8. Evaluate the quality of the unit tests for the Web front-end 
    * Functioning
9. Describe any technical debt you see in the Web front-end
    * Image files don't display correctly, link has to be clicked

### Mobile
What did the mobile front-end developer do during Phase 3'?
1. Overall evaluation of Mobile development (how was the process? was Jira used appropriately? how were tasks created? how was completion of tasks verified?)
    * Mobile development went well this week, with most functionality implemented. 
2. Describe the activities that comprise the Mobile app
    * Mobile app developer implemented file and image adding to ideas and comments. 
3. Assess the quality of the Mobile code
    * Adequate, good commenting 
4. Describe the code review process you employed for the Mobile front-end
    * The components were mostly functioning and the code was readable
5. What was the biggest issue that came up in code review of the Mobile front-end?
    * None 
6. Is the Mobile front-end code appropriately organized into files / classes / packages?
    * Yes
7. Are the dependencies in the `pubspec.yaml` (or build.gradle) file appropriate? Were there any unexpected dependencies added to the program?
    * Yes, no
8. Evaluate the quality of the unit tests for the Mobile front-end here
    * Adequate
9. Describe any technical debt you see in the Mobile front-end here
    * Problems pulling files from the mobile phone 

### Project Management
Self-evaluation of PM performance
1. When did your team meet with your mentor, and for how long?
    * Met on Tuesday for half an hour during recitation
2. Describe your use of Jira.  Did you have too much detail?  Too little?  Just enough? Did you implement policies around its use (if so, what were they?)?
    * Adequate detail, the major tasks members were working on or struggling with were marked down
3. How did you conduct team meetings?  How did your team interact outside of these meetings?
    * We conducted a team meeting on Friday to film, we were unable to meet before that because Andre was sick. Our team interacted through slack updates outside of meetings. 
4. What techniques (daily check-ins/scrums, team programming, timelines, Jira use, group design exercises) did you use to mitigate risk? Highlight any changes from last week.
    * Lots of slack updates, not many changes from last week other than a bit more communication and planning ahead. 
5. Describe any difficulties you faced in managing the interactions among your teammates. Were there any team issues that arose? If not, what do you believe is keeping things so constructive?
    * The only issue was in getting ahold of some team members at times, but when we scheduled meetings generally everyone was responsive and present. I think it went smoothly because everyone else is also committed to doing this project and we have seen that regular meetings and communication throughout the rest of the semester has helped us be successful. 
6. Describe the most significant obstacle or difficulty your team faced.
    * Some delays due to a team member getting sick, but other than that nothing gave us trouble. 
7. What is your biggest concern as you think of the future of the project? To the next sprint, if there were one? What steps can the team take to reduce your concern?
    * There will be no next sprint, so this is our completed project. The main concerns would be scalability due to caching still potentially having some issues, and making the mobile and web frontends look the same. 
8. How well did you estimate time during the early part of the phase?  How did your time estimates change as the phase progressed?
    * We estimated time fairly well and were able to complete everything we could in time. 
9. What aspects of the project would cause concern for your customer right now, if any?
    * Scalability due to caching not being fully ironed out, and also making sure files display correctly. Additionally, having the web and mobile look more similar. 
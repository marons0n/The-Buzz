# Phase 2 Sprint 9 - PM Report

## Team Information

### Core Details
- **Number**: 23
- **Name**: The Buzz
- **Mentor**: Maya Itty (mni226@lehigh.edu)
- **Weekly Meeting**: Sunday 8:00 PM EST (Without mentor)

### Team Members
| Role | Name | Email |
|------|------|-------|
| Project Manager | Warren Noubi | wdn225@lehigh.edu |
| Backend Developer | Lily Fandre | lmf226@lehigh.edu |
| Admin Developer | Stefania Dzhaman | sad823@lehigh.edu |
| Web Developer | Andre Escobedo | aee225@lehigh.edu |
| Mobile Developer | Matthew Aronson | maa362@lehigh.edu |

### Essential Links
- [Dokku URL](https://team-untitled-23.dokku.cse.lehigh.edu/)
- [Bitbucket](https://bitbucket.org/sml3/cse216_fa24_team_23)
- [Jira](https://cse216-fa24-team-23.atlassian.net)

## Sprint Status

### Meeting Schedule
- Tuesday 7:00 PM: Sprint planning and progress review
- Sunday 9:00 PM: Mentor meeting and technical discussions
- Additional: Ad-hoc pair programming sessions as needed

### Component Progress
| Component | Progress | Status | Completion |
|-----------|----------|---------|------------|
| Backend | 95% | Security testing in progress | Friday |
| Web | 90% | Finalizing CSRF protection | Thursday |
| Mobile | 85% | UI polish in progress | Wednesday |
| Admin | 100% | Completed | Done |

### Sprint Requirements Summary

#### Backend
- ✅ OAuth flow implementation
- ✅ User profile endpoints operational
- ✅ Vote system APIs implemented

#### Web
- ✅ OAuth login flow integrated
- ✅ Profile page UI completed
- ✅ Vote/comment functionality implemented

#### Mobile
- ✅ OAuth authentication working
- ✅ Profile management implemented
- ✅ Vote/comment features added

#### Admin
- ✅ User management tools completed
- ✅ Database migration scripts tested
- ✅ Table creation and population tools working

## Technical Implementation

### REST API Endpoints

#### Authentication
```
GET /auth/google
GET /auth/google/callback
POST /auth/logout
```

#### Ideas
```
GET /api/ideas
POST /api/ideas
PUT /api/ideas/:id/vote
```

#### Comments
```
GET /api/ideas/:id/comments
POST /api/ideas/:id/comments
PUT /api/ideas/:id/comments/:commentId
```

#### Profile
```
GET /api/profile
PUT /api/profile
GET /api/profile/:userId
```

### Database Schema

#### Users
```sql
CREATE TABLE Users (
    id UUID PRIMARY KEY,
    email VARCHAR,
    name VARCHAR,
    gender_identity VARCHAR,
    sexual_orientation VARCHAR,
    note TEXT,
    is_valid BOOLEAN
);
```

#### Ideas
```sql
CREATE TABLE Ideas (
    id SERIAL PRIMARY KEY,
    title VARCHAR,
    description TEXT,
    author_id UUID,
    created_at TIMESTAMP,
    is_valid BOOLEAN
);
```

#### Votes
```sql
CREATE TABLE Votes (
    id SERIAL PRIMARY KEY,
    idea_id INTEGER,
    user_id UUID,
    value INTEGER,
    created_at TIMESTAMP
);
```

#### Comments
```sql
CREATE TABLE Comments (
    id SERIAL PRIMARY KEY,
    idea_id INTEGER,
    user_id UUID,
    content TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

## Development Process

### Code Review Schedule
- Monday 2:00 PM: OAuth implementation review
- Wednesday 4:00 PM: Component integration review
- Sunday 9:00 PM: Final sprint review

### Team Practices
- Daily standup messages in Slack
- Task breakdown into 2-3 hour chunks
- Internal milestones
- Regular progress check-ins

### Current Challenges

#### Challenge 1: Cross-platform OAuth Integration
- **Status**: Resolved
- **Description**: Inconsistent token handling across platforms
- **Resolution**: Created shared authentication library

#### Challenge 2: Database Schema Migration
- **Status**: In Progress
- **Description**: Complex data model changes
- **Resolution**: Implementing incremental migration strategy

## Sprint Retrospective

### Start
- Daily integration testing
- Security review meetings
- Performance monitoring

### Stop
- Delayed PR reviews
- Skipping documentation
- Individual problem-solving

### Continue
- Daily standups
- Pair programming
- Cross-component testing

## Technical Debt

### Backend
- Need better error logging
- Some duplicate code in authentication
- Performance optimization needed
- More integration tests required

### Web
- Need better loading states
- Some prop drilling issues
- Mobile responsiveness improvements
- Test coverage gaps

### Mobile
- Offline mode needed
- Performance optimization
- Better error messaging
- Additional platform testing

### Admin
- Better error recovery needed
- Some hardcoded configurations
- Need more automated testing
- Documentation updates required

## Future Planning

### Next Phase Focus
- Scale testing
- Security hardening
- Performance optimization

### Next Sprint Priorities
- Integration completion
- Testing coverage
- Documentation updates

## Quality Metrics
- Backend: 85% test coverage
- Web: 80% test coverage
- Mobile: 75% test coverage
- Admin: 90% test coverage
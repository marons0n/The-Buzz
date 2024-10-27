# The Buzz - Phase 2 Documentation

## Table of Contents

1. [User Stories](#user-stories)
2. [User Story Tests](#user-story-tests)
3. [System Architecture](#system-architecture)
4. [UI/UX Design](#uiux-design)
5. [State Machines](#state-machines)
6. [API Routes](#api-routes)
7. [Database Design](#database-design)
8. [Testing Strategy](#testing-strategy)
9. [Technical Backlog](#technical-backlog)

## User Stories

### Authenticated User Stories

#### As an authenticated user, I want to log in with my Lehigh Google account so that I can access The Buzz securely

- **Given** I have a Lehigh email account
- **When** I click "Sign in with Google"
- **Then** I should be redirected to Google's authentication page
- **And** after successful authentication, I should be redirected back to The Buzz

#### As an authenticated user, I want to up-vote or down-vote ideas so that I can express my opinion

- **Given** I am logged in
- **When** I click the up-vote or down-vote button on an idea
- **Then** my vote should be recorded
- **And** the vote count should update accordingly
- **And** I should see my vote reflected in the UI

#### As an authenticated user, I want to add comments to ideas so that I can contribute to the discussion

- **Given** I am logged in
- **When** I add a comment to an idea
- **Then** my comment should appear under the idea
- **And** other users should be able to see my comment

#### As an authenticated user, I want to edit my profile information so that I can control my identity on the platform

- **Given** I am logged in
- **When** I update my profile information
- **Then** my changes should be saved
- **And** other users should see my updated information

### Admin Stories

#### As an admin, I want to invalidate inappropriate ideas so that I can maintain community standards

- **Given** I am logged in as an admin
- **When** I mark an idea as invalid
- **Then** the idea should no longer be visible to users

#### As an admin, I want to invalidate user accounts so that I can prevent misuse of the platform

- **Given** I am logged in as an admin
- **When** I invalidate a user account
- **Then** that user should no longer be able to log in

#### As an admin, I want to manage database tables so that I can maintain the system effectively

- **Given** I am using the admin CLI
- **When** I run table management commands
- **Then** I should be able to create, modify, and populate tables

## User Story Tests

<!-- Add your user story tests here -->

## System Architecture

<!-- Add your system architecture details here -->

## UI/UX Design

<!-- Add your UI/UX design details here -->

## State Machines

<!-- Add your state machines details here -->

## API Routes

<!-- Add your API routes details here -->

## Database Design

<!-- Add your database design details here -->

## Testing Strategy

<!-- Add your testing strategy details here -->

## Technical Backlog

<!-- Add your technical backlog details here -->
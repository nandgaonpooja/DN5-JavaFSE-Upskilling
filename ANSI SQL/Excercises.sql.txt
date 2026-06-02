-- Exercises 

--     1. User Upcoming Events 
--     Show a list of all upcoming events a user is registered for in their city, sorted by date. 

        SELECT u.user_id,u.full_name,e.title,e.start_date from Users u 
                    JOIN Registrations r ON u.user_id = r.user_id 
                    JOIN Events e ON r.event_id = e.event_id 
                    where u.city = e.city AND e.status = 'upcoming' ORDER BY e.start_date;

    
    -- 2. Top Rated Events 
    -- Identify events with the highest average rating, considering only those that have received at least 10 feedback submissions. 
        SELECT e.event_id,e.title,AVG(f.rating) as highest_avg_rating FROM Events e 
            JOIN Feedback f ON e.event_id = f.event_id
            GROUP BY e.event_id,e.title
            HAVING COUNT(*) >= 10
            AND AVG(f.rating) = 
            (SELECT MAX(avg_rating) 
                FROM 
                (SELECT AVG(rating) as avg_rating FROM Feedback f
                    GROUP BY f.event_id 
                    HAVING COUNT(*) >= 10
                ) x
            );

            

    -- 3. Inactive Users 
    -- Retrieve users who have not registered for any events in the last 90 days.

        SELECT u.user_id,u.full_name from users u 
            LEFT JOIN Registrations r ON u.user_id = r.user_id AND r.registration_date >= DATEADD(day, -90, CAST(GETDATE() AS date))
            where r.user_id IS null ;

    -- 4. Peak Session Hours 
    --Count how many sessions are scheduled between 10 AM to 12 PM for each event.

        SELECT e.event_id,e.title,COUNT(s.event_id) as session_count FROM Events e 
            JOIN Sessions s ON e.event_id = s.event_id 
            where HOUR(s.start_time) BETWEEN 10 AND 12
            GROUP BY e.event_id;

    -- 5. Most Active Cities 
    -- List the top 5 cities with the highest number of distinct user registrations.

        SELECT e.city,COUNT(DISTINCT r.user_id) as total_users FROM Events 
            JOIN Registrations r ON e.event_id=r.event_id
            GROUP BY e.city ORDER BY total_users DESC 
            LIMIT 5;

    -- 6. Event Resource Summary 
    -- Generate a report showing the number of resources (PDFs, images, links) uploaded for each event.

        SELECT event_id,title,COUNT(event_id) as count Resources 
            GROUP BY event_id
            ORDER BY event_id;

    -- 7. Low Feedback Alerts 
    -- List all users who gave feedback with a rating less than 3, along with their comments and associated event names. 

        SELECT u.user_id,u.full_name,e.title,f.comments,f.rating FROM Users u  
            JOIN Feedback f ON u.user_id = f.user_id
            JOIN Events e ON e.event_id = f.event_id
            WHERE f.rating < 3;

    -- 8. Sessions per Upcoming Event
    --  Display all upcoming events with the count of sessions scheduled for them.

        SELECT e.event_id,e.title,COUNT(s.event_id) as total_sessions FROM Events e  
            LEFT JOIN Sessions s ON e.event_id = s.event_id
            WHERE e.status = 'upcoming' 
            GROUP BY e.event_id,e.title;

    -- 9. Organizer Event Summary 
    -- For each event organizer, show the number of events created and their current status (upcoming, completed, cancelled). 
        SELECT u.user_id,u.full_name,e.status,COUNT(*) as total_events FROM Users u  
            JOIN Events e ON u.user_id = e.organizer_id
            GROUP BY  u.user_id,u.full_name,e.status;

    -- 10. Feedback Gap 
    -- Identify events that had registrations but received no feedback at all.
        SELECT DISTINCT e.event_id,e.title FROM Events e  
            JOIN Registrations r ON e.event_id = r.event_id
            LEFT JOIN Feedback f ON e.event_id = f.event_d
            WHERE f.feedback-id IS NULL;

    -- 11. Daily New User Count 
    -- Find the number of users who registered each day in the last 7 days.
        SELECT registration_date,count(*) FROM users u
            WHERE registration_date >= CURRENT_DATE - INTERVAL 7 DAY
            GROUP BY registration_date
            ORDER BY registration_date;

    -- 12. Event with Maximum Sessions 
    -- List the event (s) with the highest number of sessions. 
            SELECT e.event_id,e.title,COUNT(*) AS cnt FROM Events e 
                JOIN Sessions s ON e.event_id = s.event_id
                GROUP BY e.event_id,e.title
                HAVING COUNT(*) =(
                    SELECT MAX(cnt) FROM (
                        SELECT COUNT(*) AS cnt 
                        FROM Sessions 
                        GROUP BY event_id
                    ) x
                );

    -- 13. Average Rating per City 
    -- Calculate the average feedback rating of events conducted in each city.
        SELECT e.city,AVG(f.rating) as avg_rating FROM Events e
            JOIN Feedback f on e.event_id = f.event_id
            GROUP BY e.city

    -- 14. Most Registered Events 
    -- List top 3 events based on the total number of user registrations.
        SELECT e.event_id,e.title,COUNT(r.user_id) as users_registered FROM Events e 
            JOIN Registrations r ON e.event_id = r.event_id 
            GROUP BY e.title,e.event_id
            ORDER BY users_registered DESC
            LIMIT 3;

    -- 15. Event Session Time Conflict 
    -- Identify overlapping sessions within the same event (i.e., session start and end times that conflict). 

        SELECT s1.event_id,s1.session_id as session1,s2.session_id as session2 
            FROM Sessions s1  JOIN Sessions s2
            ON s1.event_id = s2.event_id
            AND s1.session_id < s2.session_id
            WHERE s1.start_time < s2.end_time AND s2.start_time < s1.end_time

    -- 16. Unregistered Active Users 
    -- Find users who created an account in the last 30 days but haven’t registered for any events.

        SELECT u.user_id , u.full_name FROM Users u  
            LEFT JOIN Registrations r  
            ON u.user_id = r.user_id 
            WHERE u.registration_date > CURRENT_DATE - INTERVAL 30 DAY  AND r.registration_id IS NULL

    -- 17. Multi-Session Speakers 
    -- Identify speakers who are handling more than one session across all events. 

        SELECT s1.speaker_name FROM Sessions s1
            GROUP BY s1.speaker_name
            HAVING COUNT(*) > 1;

    -- 18. Resource Availability Check 
    -- List all events that do not have any resources uploaded.

        SELECT e.event_id FROM Events e  
            LEFT JOIN Resources r ON e.event_id = r.event_id
            WHERE r.resource_id IS NULL;

    -- 19. Completed Events with Feedback Summary 
    -- For completed events, show total registrations and average feedback rating.

        SELECT e.event_id,COUNT(DISTINCT r.registration_id) AS total_registrations,AVG(rating) AS avg_rating
            FROM Events e
            JOIN Registrations r ON e.event_id = r.event_id
            JOIN Feedback f ON e.event_id = f.event_id
            GROUP BY e.event_id
            HAVING e.status = 'completed';  

    -- 20.User Engagement Index 
    -- For each user, calculate how many events they attended and how many feedbacks they submitted. 

        SELECT u.user_id,u.full_name,COUNT(DISTINCT e.event_id) AS total_events , COUNT(DISTINCT f.feedback_id) AS total_feedbacks FROM Users u  
            LEFT JOIN Registrations r ON u.user_id = r.user_id 
            LEFT JOIN Events e ON r.event_id = e.event_id AND e.status = 'completed'
            LEFT JOIN Feedback f ON u.user_id = f.user_id
            GROUP BY u.user_id,u.full_name;

    -- 21.Top Feedback Providers 
    -- List top 5 users who have submitted the most feedback entries. 
        SELECT u.user_id,u.full_name,COUNT(f.feedback_id) AS total_feedbacks FROM Users u  
            JOIN Feedback f ON u.user_id = f.user_id 
            GROUP BY u.user_id,u.full_name
            ORDER BY total_feedbacks DESC 
            LIMIT 5;

    -- 22. Duplicate Registrations Check 
    -- Detect if a user has been registered more than once for the same event

        SELECT user_id,COUNT(user_id) as total_registered FROM  Registrations
            GROUP BY u.user_id,r.event_id
            HAVING total_registered > 1;
            
    -- 23. Registration Trends 
    -- Show a month-wise registration count trend over the past 12 months.

        SELECT DATE_FORMAT(registration_date,'%Y-%m') AS mnth,COUNT(*) AS total_count
            FROM Registrations
            WHERE registration_date >= CURRENT_DATE - INTERVAL
            12 MONTH
            GROUP BY DATE_FORMAT
            (registration_date,'%Y-%m')
            ORDER BY mnth;

    -- 24. Average Session Duration per Event 
    -- Compute the average duration (in minutes) of sessions in each event.

        SELECT e.event_id,e.title,ROUND(AVG(TIMESTAMPDIFF(MINUTE,s.start_time,s.end_time)), 2) AS avg_duration_minutes
            FROM Events e
            JOIN Sessions s
            ON e.event_id = s.event_id
            GROUP BY e.event_id, e.title;

    -- 25. Events Without Sessions 
    -- List all events that currently have no sessions scheduled under them.

        SELECT e.event_id,e.title
            FROM Events e
            LEFT JOIN Sessions s
            ON e.event_id = s.event_id
            WHERE s.session_id IS NULL;
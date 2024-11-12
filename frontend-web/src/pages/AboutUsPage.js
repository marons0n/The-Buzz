import React from 'react';
import { motion } from 'framer-motion';
import './AboutUsPage.css';

const AboutUsPage = () => {
  const fadeIn = {
    initial: { opacity: 0, y: 20 },
    animate: { opacity: 1, y: 0 },
    transition: { duration: 0.6 }
  };

  const teamMembers = [
    { name: "Warren Noubi", role: "Web Frontend Dev", image: "/api/placeholder/150/150" },
    { name: "Team Member 2", role: "Backend Dev", image: "/api/placeholder/150/150" },
    { name: "Team Member 3", role: "Mobile Frontend Dev", image: "/api/placeholder/150/150" },
    { name: "Team Member 4", role: "Admin", image: "/api/placeholder/150/150" },
    { name: "Team Member 5", role: "Project Manager", image: "/api/placeholder/150/150" }
  ];

  return (
    <div className="about-us-page">
      <motion.section className="hero" {...fadeIn}>
        <h1>About The Buzz</h1>
        <p>A CSE 216 Project: Innovating Workplace Collaboration</p>
      </motion.section>

      <motion.section className="project-overview" {...fadeIn}>
        <h2>Project Overview</h2>
        <p>The Buzz is a cutting-edge workplace collaboration platform developed as part of the CSE 216 course at our university. Our team of five passionate students is working to create a solution that empowers employees to share ideas and innovate together, regardless of their location or role within a company.</p>
      </motion.section>

      <motion.section className="our-goal" {...fadeIn}>
        <h2>Our Goal</h2>
        <p>Our primary objective is to design and implement a user-friendly, secure, and scalable application that facilitates idea sharing and collaboration. We're focusing on creating a platform that not only meets the technical requirements of our course but also addresses real-world challenges in modern workplace communication.</p>
      </motion.section>

      <motion.section className="our-team" {...fadeIn}>
        <h2>Meet Our Team</h2>
        <div className="team-grid">
          {teamMembers.map((member, index) => (
            <motion.div 
              key={index} 
              className="team-member"
              initial={{ opacity: 0, scale: 0.8 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ duration: 0.5, delay: index * 0.1 }}
            >
              <img src={member.image} alt={member.name} />
              <h3>{member.name}</h3>
              <p>{member.role}</p>
            </motion.div>
          ))}
        </div>
      </motion.section>

      <motion.section className="key-features" {...fadeIn}>
        <h2>Key Features</h2>
        <ul>
          <li><strong>Idea Sharing:</strong> Post and share innovative thoughts with colleagues.</li>
          <li><strong>Engagement System:</strong> Like and interact with ideas from team members.</li>
          <li><strong>Global Accessibility:</strong> Connect with coworkers across different locations.</li>
          <li><strong>Secure Platform:</strong> Ensure data privacy and protection.</li>
        </ul>
      </motion.section>

      <motion.section className="technology-stack" {...fadeIn}>
        <h2>Our Technology Stack</h2>
        <p>We're leveraging modern web technologies to build The Buzz:</p>
        <ul>
          <li><strong>Frontend:</strong> React.js with Framer Motion for animations</li>
          <li><strong>Backend:</strong>  Java Spring</li>
          <li><strong>Database:</strong>  PostgreSQL</li>
          <li><strong>Authentication:</strong>   JWT</li>
        </ul>
      </motion.section>

      <motion.section className="project-status" {...fadeIn}>
        <h2>Project Status</h2>
        <p>We are currently in the [development/testing/finalization] phase of our project. Our team is working diligently to deliver a robust and innovative solution by the end of the semester.</p>
      </motion.section>

      <motion.section className="acknowledgments" {...fadeIn}>
        <h2>Acknowledgments</h2>
        <p>We'd like to express our gratitude to our course instructor, teaching assistants, and peers for their guidance and support throughout this project.</p>
      </motion.section>
    </div>
  );
};

export default AboutUsPage;
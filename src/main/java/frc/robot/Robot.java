/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot {
  private final DifferentialDrive m_robotDrive = new DifferentialDrive(new PWMVictorSPX(0), new PWMVictorSPX(1));
  private final Joystick m_stick = new Joystick(0);
  private final Joystick x_stick = new Joystick(1);
  private final Timer m_timer = new Timer();
  private final Compressor comp = new Compressor(0);
  private final Solenoid sol1 = new Solenoid(0);
 
  //DifferentialDrive myDrive;
 
  
  
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    
  }

  /**
   * This function is run once each time the robot enters autonomous mode.
   */
  @Override
  public void autonomousInit() {
    m_timer.reset();
    m_timer.start();
    System.out.println("Autonomous Engaged");
    comp.setClosedLoopControl(true);
    comp.enabled();
    sol1.set(true);
    System.out.println(comp.getCompressorCurrent());
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    /**
    // Drive for 2 seconds
    Double speed = 0.5; //speed of the robot (Going to be multiplied by robot at %100 speed)
    if (m_timer.get() < 3.0) {
    m_robotDrive.tankDrive(1* speed, 1*speed); // drive forwards
    m_robotDrive.tankDrive(-1*speed, -1*speed); // drive forwards
    m_robotDrive.tankDrive(0, 1*speed); //turns ????
    m_robotDrive.tankDrive(1*speed, 0); //turns ????
    } else {
      m_robotDrive.stopMotor(); // stop robot
      System.out.println("Autonomous Stopped");
    }
     */
  }

  /**
   * This function is called once each time the robot enters teleoperated mode.
   */
  @Override
  public void teleopInit() {
    System.out.println("Manual Control Engaged");
  }

  /**
   * This function is called periodically during teleoperated mode.
   */
  @Override
  public void teleopPeriodic() {
    //m_robotDrive.arcadeDrive(m_stick.getY()`, m_stick.getX()); 
    while (isOperatorControl()&& isEnabled()){
    m_robotDrive.tankDrive((m_stick.getY()*-1), (x_stick.getY()*-1));//test
    Timer.delay(0.01);
    }
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
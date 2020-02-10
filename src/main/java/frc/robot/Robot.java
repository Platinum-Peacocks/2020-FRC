/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;



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
  private final Compressor comp = new Compressor(1);
  private final DoubleSolenoid sol1 = new DoubleSolenoid(1, 0, 1);
  private final XboxController cont = new XboxController(2);
  private final ColorSensorV3 color = new ColorSensorV3(I2C.Port.kOnboard);
  //DifferentialDrive myDrive;
 
  
  
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    comp.clearAllPCMStickyFaults();
    comp.setClosedLoopControl(true);
    comp.start();
    
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
    comp.start();
    //sol1.set(Value.kReverse);
  }

  /** 
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    // Drive for 2 seconds
    /** 
    Double speed = 0.5; //speed of the robot (Going to be multiplied by robot at %100 speed)
    if (m_timer.get() < 3.0) {
    m_robotDrive.tankDrive(1* speed, 1*speed); // drive forwards
    m_robotDrive.tankDrive(-1*speed, -1*speed); // drive forwards
    m_robotDrive.tankDrive(0, 1*speed); //turns ????
    m_robotDrive.tankDrive(1*speed, 0); //turns ????
    } else {
      m_robotDrive.stopMotor(); // stop robot
      System.out.println("Autonomous Stopped");
    }*/
  }

  /**
   * This function is called once each time the robot enters teleoperated mode.
   */
  @Override
  public void teleopInit() {
    comp.setClosedLoopControl(true);
    comp.start();
    System.out.println("Manual Control Engaged");
  }

  /**
   * This function is called periodically during teleoperated mode.
   */
  @Override
  public void teleopPeriodic() {
    //m_robotDrive.arcadeDrive(m_stick.getY()`, m_stick.getX()); 
  
  //Movement Controls
  //m_robotDrive.tankDrive((m_stick.getY()*-1), (x_stick.getY()*-1));//test
  //Timer.delay(0.01); 
  
  //Gamepad Solenoid Control
  if(cont.getAButton()) {
    sol1.set(Value.kForward);
    }
  else if(cont.getBButton()) {
    sol1.set(Value.kReverse);
  }

  //Color Sensor Values
  Color detectedColor = color.getColor();
  double IR = color.getIR();
  double[] colorArray = {detectedColor.red, detectedColor.green, detectedColor.blue};

  System.out.println(colorArray[0] + "    " + colorArray[1] + "    " + colorArray[2]);
  //.3, .5, .1 Yellow
  //.5, .3, .1 Red
  //.1, .5, .2 Green
  //.1, .4, .4 Blue
  if(colorArray[0] > .3 && colorArray[0] < .4 && colorArray[1] > .5 && colorArray[1] < .6 && colorArray[2] > .1 && colorArray[2] < .2){
    sol1.set(Value.kForward);
    System.out.println("Yellow");
  }
  if(colorArray[0] > .5 && colorArray[0] < .6 && colorArray[1] > .3 && colorArray[1] < .4 && colorArray[2] > .1 && colorArray[2] < .2){
    sol1.set(Value.kForward);
    System.out.println("Red");
  }
  if(colorArray[0] > .1 && colorArray[0] < .2 && colorArray[1] > .5 && colorArray[1] < .6 && colorArray[2] > .2 && colorArray[2] < .3){
    sol1.set(Value.kReverse);
    System.out.println("Green");
  }
  if(colorArray[0] > .1 && colorArray[0] < .2 && colorArray[1] > .4 && colorArray[1] < .5 && colorArray[2] > .4 && colorArray[2] < .5){
    sol1.set(Value.kReverse);
    System.out.println("Blue");
  }
} 
  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    
  }
}
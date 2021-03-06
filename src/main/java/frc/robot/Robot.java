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
import edu.wpi.first.wpilibj.GenericHID.Hand;
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
  //motors and Drive
  private final DifferentialDrive m_robotDrive = new DifferentialDrive(new PWMVictorSPX(0), new PWMVictorSPX(1));
 PWMVictorSPX intakeMotor = new PWMVictorSPX(2);
 PWMVictorSPX feedMotor = new PWMVictorSPX(3);
 PWMVictorSPX climbMotor = new PWMVictorSPX(4);
 PWMVictorSPX colorMotor = new PWMVictorSPX(5);
 
  //Controllers
  private final Joystick m_stick = new Joystick(0);
  private final Joystick x_stick = new Joystick(1);
  private final XboxController cont = new XboxController(2);

  //Autonomous
  private final Timer m_timer = new Timer();

  //Pneumatics
  private final Compressor comp = new Compressor(1);
  private final DoubleSolenoid intakeSol = new DoubleSolenoid(1, 0, 1);
  private final DoubleSolenoid feederSol = new DoubleSolenoid(1, 2, 3);
  private final DoubleSolenoid climbSol = new DoubleSolenoid(1, 4, 5);
  
  //sensors
  private final ColorSensorV3 color = new ColorSensorV3(I2C.Port.kOnboard);
 
  
  
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    comp.clearAllPCMStickyFaults(); //gets rid of sticky faults
    comp.setClosedLoopControl(true); //compressor default
    comp.start(); //starts compressor
    
  }

  /**
   * This function is run once each time the robot enters autonomous mode.
   */
  @Override
  public void autonomousInit() {
    m_timer.reset(); //resets timer for autonomous
    m_timer.start(); //starts timer for autonomous
    System.out.println("Autonomous Engaged");
    comp.setClosedLoopControl(true); //PCM will automaticallyturn the compressor on or off when it is below or at certain pressure
    comp.start(); //starts compressor
  }

  /* 
   This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
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
    comp.start(); //starts compressor
    climbSol.set(Value.kReverse);
    feederSol.set(Value.kForward);
    intakeSol.set(Value.kReverse);
    System.out.println("Manual Control Engaged");
    
  }

  /**
   * This function is called periodically during teleoperated mode.
   */
  @Override
  public void teleopPeriodic() {
  
  //Movement Controls (tank drive)
  m_robotDrive.tankDrive((m_stick.getY()*-1), (x_stick.getY()*-1));
  Timer.delay(0.01); 
  
  //Gamepad Controls
 
   
  /*else if(cont.getBumper(Hand.kLeft)) {//LB
    intakeSol.set(Value.kReverse);
  }*/
 

  /*if(cont.getYButton()) {//Y
    intakeMotor.set(-1.0);
  }*/
 
   if(cont.getBButton()) {//B
    feedMotor.set(1.0);
    feederSol.set(Value.kReverse);
  }
  else if(cont.getTriggerAxis(Hand.kLeft) > .1) {//LT
    feedMotor.set(-1.0);
  }

  else if(cont.getY(Hand.kLeft) > .6) {
    climbSol.set(Value.kReverse);
  }
  
  else if(cont.getY(Hand.kLeft) < -.2) {
    climbSol.set(Value.kForward);
  }
  
  else if(cont.getStickButton(Hand.kRight)) {//right stick
    climbMotor.set(1.0);
  }
  else {
   
    feederSol.set(Value.kForward);
    climbMotor.stopMotor();

    if(cont.getXButton()) { 
      intakeSol.set(Value.kForward);
     /* feederSol.set(Value.kForward);*/
      //feedMotor.set(1.0);
      //intakeMotor.set(1.0);
    }
    else if (!cont.getXButton()) {
      intakeSol.set(Value.kReverse);
    }
    if(cont.getBumper(Hand.kRight)) {//RB
    feedMotor.set(1.0);
    intakeMotor.set(1.0);
    }
    else if (cont.getBumper(Hand.kLeft)) {//LB
      intakeMotor.set(-1.0);
    }else{
    intakeMotor.stopMotor();
    feedMotor.stopMotor();
    }
  }
  
  
  }
}
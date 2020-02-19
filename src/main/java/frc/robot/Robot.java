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
  
  //Gamepad Solenoid Control
  if(cont.getAButton()) {
    feederSol.set(Value.kForward); //piston for ramp
    intakeMotor.set(0.5); //motor for intake to spin (Mobius wheels)
    feedMotor.set(0.5); //big top belt to hold balls

    }
  else {
    feederSol.set(Value.kReverse);
    intakeMotor.stopMotor();

    if(cont.getBButton()) {
      feedMotor.set(0.5); //Belt to score balls
    }
    else {
      feedMotor.stopMotor();
    }
  }
  if(cont.getXButton()) {
    intakeSol.set(Value.kForward); //intake system deployed piston/front arm with wheels
  }
  else {
    intakeSol.set(Value.kReverse);
  }

  if(cont.getBumper(Hand.kLeft)) {
    climbMotor.set(-0.5); //run motor to climb
  }
  else {
    climbMotor.stopMotor();
  }

  if(cont.getYButton()) {
    climbSol.set(Value.kForward); //climb pole release
  }
  else {
    climbSol.set(Value.kReverse);
  }
  
  //Color Sensor Values
  Color detectedColor = color.getColor();
  double IR = color.getIR();
  double[] colorArray = {detectedColor.red, detectedColor.green, detectedColor.blue};

  //color sensor operations (IN TESTING!!!!)
  boolean Yellow = false;
  boolean Red = false;
  boolean Green = false;
  boolean Blue = false;

  boolean alreadyExecutedY = false;
  boolean alreadyExecutedR = false;
  boolean alreadyExecutedG = false;
  boolean alreadyExecutedB = false;

  boolean colorMotorB = false;

  int yellowN = 0;
  int redN = 0;
  int greenN = 0;
  int blueN = 0;
  
  if(Yellow == true && alreadyExecutedY == false){
    alreadyExecutedY = true;
    alreadyExecutedR = false;
    alreadyExecutedG = false;
    alreadyExecutedB = false;
    yellowN = yellowN + 1;
    System.out.println(yellowN);

  } else if(Red == true && alreadyExecutedR == false){
    alreadyExecutedY = false;
    alreadyExecutedR = true;
    alreadyExecutedG = false;
    alreadyExecutedB = false;
    redN = redN + 1;
    System.out.println(redN);

  }else if(Green == true && alreadyExecutedG == false){
    alreadyExecutedY = false;
    alreadyExecutedR = false;
    alreadyExecutedG = true;
    alreadyExecutedB = false;
    greenN = greenN + 1;
    System.out.println(greenN);

  }else if(Blue == true && alreadyExecutedB == false){
    alreadyExecutedY = false;
    alreadyExecutedR = false;
    alreadyExecutedG = false;
    alreadyExecutedB = true;
    blueN = blueN + 1;
    System.out.println(blueN);
  }

  int fullRotate = 0; //amount of rotations made in real time

  //editable variables
  int targetColor = yellowN; // color you want to stop on
  int targetRotate = 2; //two equals one full rotation because colors are on wheel twice

  if(yellowN == targetRotate && redN == targetRotate && greenN == targetRotate && blueN == targetRotate){
    fullRotate = 1;
  }
  if(fullRotate == targetRotate && targetColor == targetRotate + 1){
    //stop the motor
    colorMotorB = true;
  }
  if(cont.getBumper(Hand.kRight) && colorMotorB == false){
    //run the colormotor
    colorMotor.set(0.5);
    //have to hold
  }else{
    //stop color motor
    colorMotor.stopMotor();
  }
  //END OF TESTING


  //sensor system
  System.out.println(colorArray[0] + "    " + colorArray[1] + "    " + colorArray[2]);
  //.3, .5, .1 Yellow
  //.5, .3, .1 Red
  //.1, .5, .2 Green
  //.1, .4, .4 Blue
  if(colorArray[0] > .3 && colorArray[0] < .4) {
    System.out.println("Yellow");

    Yellow = true;
    Red = false;
    Green = false;
    Blue = false;
  }
  if(colorArray[0] > .5 && colorArray[0] < .6){
    System.out.println("Red");

    Yellow = false;
    Red = true;
    Green = false;
    Blue = false;
  }
  if(colorArray[0] > .1 && colorArray[0] < .2 && colorArray[1] > .5 && colorArray[1] < .6){
    System.out.println("Green");

    Yellow = false;
    Red = false;
    Green = true;
    Blue = false;
  }
  if(colorArray[0] > .1 && colorArray[0] < .2 && colorArray[1] > .4 && colorArray[1] < .5){
    System.out.println("Blue");

    Yellow = false;
    Red = false;
    Green = false;
    Blue = true;
  }
  
} 
  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    
  }
}
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.beans.Encoder;

import com.kauailabs.navx.frc.*;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANSparkMax;
import com.revrobotics.EncoderType;
import com.revrobotics.REVLibError;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxRelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxRelativeEncoder.Type;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Constants;

public class Drivetrain extends SubsystemBase {
  
  final AHRS navx_gyro = new AHRS();
  final Field2d field_2d = new Field2d();
  final DifferentialDriveOdometry drive_odometry = new DifferentialDriveOdometry(navx_gyro.getRotation2d());

  final double RPM_TO_MS_RATIO = (Constants.DRIVE.WHEEL_DIAMETER_METER * Math.PI) / 60; // RPM to m/s

  final CANSparkMax[] DRIVETRAIN_LEFT = {new CANSparkMax(Constants.CAN_ID.DRIVETRAIN_LEFT[0], MotorType.kBrushless), 
  new CANSparkMax(Constants.CAN_ID.DRIVETRAIN_LEFT[1], MotorType.kBrushless)};
  final CANSparkMax[] DRIVETRAIN_RIGHT = {new CANSparkMax(Constants.CAN_ID.DRIVETRAIN_RIGHT[0], MotorType.kBrushless), 
  new CANSparkMax(Constants.CAN_ID.DRIVETRAIN_RIGHT[1], MotorType.kBrushless)};

  DifferentialDrive differential_drive = null;
  MotorControllerGroup left_motor_controller = null;
  MotorControllerGroup right_motor_controller = null;

  RelativeEncoder[] left_encoders = {DRIVETRAIN_LEFT[0].getEncoder(Type.kHallSensor, 42), DRIVETRAIN_LEFT[1].getEncoder(Type.kHallSensor, 42)};
  RelativeEncoder[] right_encoders = {DRIVETRAIN_RIGHT[0].getEncoder(Type.kHallSensor, 42), DRIVETRAIN_RIGHT[1].getEncoder(Type.kHallSensor, 42)};
  
  public Drivetrain(){
    
    DRIVETRAIN_RIGHT[0].setInverted(true);
    DRIVETRAIN_RIGHT[1].setInverted(true);

    left_motor_controller = new MotorControllerGroup(DRIVETRAIN_LEFT[0], DRIVETRAIN_LEFT[1]);
    right_motor_controller = new MotorControllerGroup(DRIVETRAIN_RIGHT[0], DRIVETRAIN_RIGHT[1]);
    differential_drive = new DifferentialDrive(left_motor_controller, right_motor_controller);
  }

  public void TankDrive(double left_speed, double right_speed)
  {
    if(differential_drive != null)
    {
      differential_drive.tankDrive(left_speed, right_speed);
    }
  }

  public void resetOdometry(Pose2d pose) {
    drive_odometry.resetPosition(pose, navx_gyro.getRotation2d());
  }

  public void tankDriveVolts(double left_volts, double right_volts) {
    left_motor_controller.setVoltage(left_volts);
    right_motor_controller.setVoltage(right_volts);
    differential_drive.feed();
  }

  public void displayVoltages() {
    SmartDashboard.putNumber("Left Drive Front: ", DRIVETRAIN_LEFT[0].getBusVoltage());
    SmartDashboard.putNumber("Left Drive Back: ", DRIVETRAIN_LEFT[1].getBusVoltage());
    SmartDashboard.putNumber("Right Drive Front: ", DRIVETRAIN_RIGHT[0].getBusVoltage());
    SmartDashboard.putNumber("Right Drive Back: ", DRIVETRAIN_RIGHT[1].getBusVoltage());
  }

  public Pose2d getPose() {
    return drive_odometry.getPoseMeters();
  }

  private double getAverageRPM(RelativeEncoder[] encoder_pair) { 
    // Using defualt units for velocity (RPM)
    return (encoder_pair[0].getVelocity() + encoder_pair[1].getVelocity()) / 2;
  }

  public DifferentialDriveWheelSpeeds getWheelSpeeds() { 
    return new DifferentialDriveWheelSpeeds(getAverageRPM(left_encoders) * RPM_TO_MS_RATIO, getAverageRPM(right_encoders) * RPM_TO_MS_RATIO);
  }

  public double getNavXAngleZ() {
    return navx_gyro.getAngle();
  }

  public double getNavXAccelX() {
    return navx_gyro.getRawAccelX();
  }

  public double getNavXAccelY() {
    return navx_gyro.getRawAccelY();
  }

  public double getNavXAccelZ() {
    return navx_gyro.getRawAccelZ();
  }

  public void resetGyro() {
    navx_gyro.reset();
  }


  public void setMaxOutput(double max_output) {
    differential_drive.setMaxOutput(max_output);
  }

  public void zeroHeading() {
    navx_gyro.reset();
  }

  public double getHeading() {
    return navx_gyro.getRotation2d().getDegrees();
  }

  public double getTurnRate() {
    return navx_gyro.getRate();
  }

  private double getAverageEncoderDistance(RelativeEncoder[] encoder_pair) {
    return ((encoder_pair[0].getPosition() + encoder_pair[1].getPosition()) / 2) * Constants.DRIVE.WHEEL_DIAMETER_METER * Math.PI;
  }

  @Override
  public void periodic() {
    drive_odometry.update(navx_gyro.getRotation2d(), getAverageEncoderDistance(left_encoders), getAverageEncoderDistance(right_encoders));
    field_2d.setRobotPose(getPose());
  }

  @Override
  public void simulationPeriodic() {}
}
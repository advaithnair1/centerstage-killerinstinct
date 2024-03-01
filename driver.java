package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

        ;import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name="CENTERSTAGE DRIVER CONTROL FINAL4", group="Linear Opmode")

public class driver extends LinearOpMode {
    boolean lastA = false;                      // Use to track the prior button state.
    boolean lastLB = false;                     // Use to track the prior button state.
    boolean highLevel = false;                  // used to prevent multiple level-based rumbles.
    boolean secondHalf = false;                 // Use to prevent multiple half-time warning rumbles.

    private DistanceSensor sensorDistanceRight;
    private DistanceSensor sensorDistanceLeft;

    Gamepad.RumbleEffect customRumbleLeft;    // Use to build a custom rumble sequence.


    Gamepad.RumbleEffect customRumbleRight;    // Use to build a custom rumble sequence.


    private ElapsedTime runtime = new ElapsedTime();
    @Override
    public void runOpMode() throws InterruptedException {

        customRumbleLeft = new Gamepad.RumbleEffect.Builder()

                .addStep(1.0, 0.0, 500)  //  Rumble left motor 100% for 500 mSec

                .build();

        customRumbleRight = new Gamepad.RumbleEffect.Builder()

                .addStep(0.0, 1.0, 500)  //  Rumble right motor 100% for 500 mSec

                .build();

        // Declaration and mapping of lift motor.

        // Declaration and mapping of drive motors.
        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("motorBackRight");


        sensorDistanceRight = hardwareMap.get(DistanceSensor.class, "sensor_distance_right");
        sensorDistanceLeft = hardwareMap.get(DistanceSensor.class, "sensor_distance_left");


        DcMotor liftRightMotor = hardwareMap.dcMotor.get("liftRightMotor");

        DcMotor liftLeftMotor = hardwareMap.dcMotor.get("liftLeftMotor");



        //Declaration and mapping of the claw Servo
        Servo rightliftServo = hardwareMap.get(Servo.class, "rightliftServo");
        //Servo leftliftServo = hardwareMap.get(Servo.class, "leftliftServo");

        Servo LeftClawServo = hardwareMap.get(Servo.class, "LeftClawServo");

        Servo RightClawServo = hardwareMap.get(Servo.class, "RightClawServo");

        Servo planeServo = hardwareMap.get(Servo.class, "planeServo");
        planeServo.setPosition(0);

        rightliftServo.setPosition(0.28);

        // Reverse the right side motors
        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotor.Direction.FORWARD);
        motorBackRight.setDirection(DcMotor.Direction.FORWARD);
        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        double position = 0;
        double right1 = 0;
        double left1 = 0;

        boolean left = false;
        boolean right = false;
        double openingtime = 0;
        ElapsedTime runtime = new ElapsedTime();



        resetRuntime();
        while (opModeIsActive()) {
            openingtime = runtime.seconds();

            double proxyright = sensorDistanceRight.getDistance(DistanceUnit.MM);
            double proxyleft = sensorDistanceLeft.getDistance(DistanceUnit.MM);



            //Drive Motor Code!
            //Not to be messed with under any circumstances.

            double drive = -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x * 1.1;
            double rotate = gamepad1.right_stick_x;

            // Stupid Code
            double drive2 = -gamepad2.left_stick_y;
            double strafe2 = gamepad2.left_stick_x * 1.1;
            double rotate2 = gamepad2.right_stick_x;

            telemetry.addLine("left stick y" + drive);
            telemetry.addLine("left stick x" + strafe);
            telemetry.addLine("right stick x" + rotate);
            telemetry.addLine("servo pos: " + rightliftServo.getPosition());

            telemetry.addLine("lift right: " + liftRightMotor.getPower());
            telemetry.addLine("lift left: " + liftLeftMotor.getPower());

            double frontLeftPower = drive + strafe + rotate;
            double frontRightPower = drive - strafe - rotate;
            double backLeftPower = drive - strafe + rotate;
            double backRightPower = drive + strafe - rotate;

            frontLeftPower += 0.05;

            double frontLeftPower2 = drive2 + strafe2 + rotate2;
            double frontRightPower2 = drive2 - strafe2 - rotate2;
            double backLeftPower2 = drive2 - strafe2 + rotate2;
            double backRightPower2 = drive2 + strafe2 - rotate2;

            frontLeftPower2 += 0.05;

            double maxPower = Math.max(Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower)),
                    Math.max(Math.abs(backLeftPower), Math.abs(backRightPower)));

            double maxPower2 = Math.max(Math.max(Math.abs(frontLeftPower2), Math.abs(frontRightPower2)),
                    Math.max(Math.abs(backLeftPower2), Math.abs(backRightPower2)));

            if (maxPower > 1.0) {
                frontLeftPower /= maxPower;
                frontRightPower /= maxPower;
                backLeftPower /= maxPower;
                backRightPower /= maxPower;
            }

            if (maxPower > 1.0) {
                frontLeftPower2 /= maxPower2;
                frontRightPower2 /= maxPower2;
                backLeftPower2 /= maxPower2;
                backRightPower2 /= maxPower2;
            }


            if(gamepad1.left_stick_y >= 0.1 || gamepad1.left_stick_x >= 0.1 || gamepad1.left_stick_y <= -0.1 || gamepad1.left_stick_x <= -0.1 || gamepad1.right_stick_y >= 0.1 || gamepad1.right_stick_x >= 0.1 || gamepad1.right_stick_y <= -0.1 || gamepad1.right_stick_x <= -0.1  )
            {
                motorFrontLeft.setPower(frontLeftPower);
                motorBackLeft.setPower(backLeftPower);
                motorFrontRight.setPower(frontRightPower);
                motorBackRight.setPower(backRightPower);
            }

            else if(gamepad2.left_stick_y >= 0.1 || gamepad2.left_stick_x >= 0.1 || gamepad2.left_stick_y <= -0.1 || gamepad2.left_stick_x <= -0.1 || gamepad2.right_stick_y >= 0.1 || gamepad2.right_stick_x >= 0.1 || gamepad2.right_stick_y <= -0.1 || gamepad2.right_stick_x <= -0.1  )
            {
                motorFrontLeft.setPower(frontLeftPower2 * 0.4);
                motorBackLeft.setPower(backLeftPower2 * 0.4);
                motorFrontRight.setPower(frontRightPower2 * 0.4);
                motorBackRight.setPower(backRightPower2 * 0.4);
            }

            else
            {
                motorFrontLeft.setPower(0);
                motorBackLeft.setPower(0);
                motorFrontRight.setPower(0);
                motorBackRight.setPower(0);
            }
            //Lift Motor Code
            // Find starting height for lift and find power at that point
            // Find final height and power and map that to the claw orientation (position 0 < x < 1)



            if (gamepad1.left_trigger > 0.1){
                liftRightMotor.setPower(0.6 *  gamepad1.left_trigger);
                liftLeftMotor.setPower(-0.6 * gamepad1.left_trigger);
            } else if (gamepad1.right_trigger > 0.1)
            {
                liftRightMotor.setPower(-0.6 * gamepad1.right_trigger);
                liftLeftMotor.setPower(0.6 * gamepad1.right_trigger);
            } else {
                liftRightMotor.setPower(-0.10);
                liftLeftMotor.setPower(0.1);
            }

            //Stupid Code

            if (gamepad2.left_trigger > 0.1){
                liftRightMotor.setPower(0.2 * gamepad2.left_trigger);
                liftLeftMotor.setPower(-0.2 * gamepad2.left_trigger);
            } else if (gamepad2.right_trigger > 0.1)
            {
                liftRightMotor.setPower(-0.6 * gamepad2.right_trigger);
                liftLeftMotor.setPower(0.6 * gamepad2.right_trigger);
            }

            // double liftServoPowerUp  = aamepad2.right_trigger;
            // double liftServoPowerDown = // gamepad2.left_trigger;


            if (gamepad1.left_bumper == true && position >= 0.2 || gamepad2.left_bumper == true && position >= 0.2){
                position -= 0.1;
                sleep(30);
                rightliftServo.setPosition(position);
            }

            if (gamepad1.right_bumper == true && position < 1.00 || gamepad2.right_bumper == true && position < 1.00){
                position += 0.1;
                sleep(30);
                rightliftServo.setPosition(position);
            }

            /*

            if (liftServoPowerUp > 0 && position < 1) {
                position += 0.05;
                leftliftServo.setPosition(position);
            }

            if (liftServoPowerDown > 0 && position > 0){
                position -= 0.05;
                leftliftsetPosition(position);
            }



            */


            /*

            if(gamepad1.x == true && position < 1)
            {
                position = position + 0.1;
                sleep(1200);
                rightliftServo.setPosition(position);
                //leftliftServo.setPosition(position);
            }
            else if(gamepad1.y == true && position > 0)
            {
                position = position - 0.1;
                sleep(1200);
                rightliftServo.setPosition(position);
                //leftliftServo.setPosition(position);
            }

            */

            /*if(gamepad2.a){
                //Lift goes down.
                liftMotor.setPower(linearSlidePower);
            }else if(gamepad2.b){
                //Lift goes up.
                liftMotor.setPower(linearSlidePower);
            }else{
                liftMotor.setPower(0);
            }
            //else{
             //   liftMotor.setPower(0.1);
            //}*/

            //May have to add some ambient power to keep the claw up. Only if we have to though. It would be rather inconvenient otherwise

            //Claw Servo Code

            // double clawPowerDown = gamepad2.left_trigger;
            // double clawPowerUp = gamepad2.right_trigger;

            /*

            if (clawPowerDo
            }
             */

            //setPosition is between 0 and 1



            if(gamepad1.x && left1 == 1 || gamepad2.x && left1 == 1) {
                //open the left claw
                LeftClawServo.setPosition(0);
                left1 = 0;
                sleep(300);
            }
            else if (gamepad1.x && left1 == 0 || gamepad2.x && left1 == 0){
                LeftClawServo.setPosition(0.2);

                left1 = 1;
                runtime.reset();
                openingtime = 0;
                sleep(300);
            }
            else if (proxyleft < 25.5 && left1 != 0 && openingtime >= 1.3)
            {
                LeftClawServo.setPosition(0);
                left1 = 0;

                gamepad1.rumble(0,1, 1000);
                gamepad2.rumble(0,1, 1000);
            }

            if(gamepad1.a && right1 == 1 || gamepad2.a && right1 == 1) {
                //open the left claw
                RightClawServo.setPosition(0.9);
                right1 = 0;
                sleep(300);



            }
            else if (gamepad1.a && right1 == 0 || gamepad2.a && right1 == 0){
                RightClawServo.setPosition(0.7);

                right1 = 1;
                runtime.reset();
                openingtime = 0;
                sleep(300);
            }
            else if (proxyright < 25.5 && right1 != 0 && openingtime >= 1.3)
            {
                RightClawServo.setPosition(0.92);
                right1 = 0;
                gamepad1.rumble(1,0, 3000);
                gamepad2.rumble(1,0, 3000);


            }



            if(gamepad1.dpad_up || gamepad2.dpad_up) {
                planeServo.setPosition(0.7);
            }

            if (gamepad1.dpad_down || gamepad2.dpad_down){
                resetRuntime();
                while (getRuntime() <= 30)
                {
                    liftRightMotor.setPower(1);
                    liftLeftMotor.setPower(-1);
                }
            }

            telemetry.addLine("Front Left Motor Power" + frontLeftPower);
            telemetry.addLine("Front Right Motor Power" + frontRightPower);
            telemetry.addLine("Back Left Motor Power" + backLeftPower);
            telemetry.addLine("Back Right Motor Power" + backRightPower);
            telemetry.addLine("Left Claw Pos: " + LeftClawServo.getPosition());
            telemetry.addLine("Right Claw Pos: " + RightClawServo.getPosition());
            telemetry.addLine("distanceRight:" + proxyright);
            telemetry.addLine("distanceLeft:" + proxyleft);
            telemetry.addLine("ServoSafetyTime:" + openingtime);
            telemetry.addLine("PositionLiftRight" + liftRightMotor.getCurrentPosition());
            telemetry.addLine("PositionLiftLeft" + liftLeftMotor.getCurrentPosition());
            telemetry.update();

        }
    }
}
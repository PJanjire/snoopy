<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Reset Password</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f6f6f6;
            margin: 0;
            padding: 0;
        }

        .container {
            width: 600px;
            margin: 30px auto;
            background: #ffffff;
            padding: 25px;
            border-radius: 8px;
            box-shadow: 0px 2px 8px rgba(0,0,0,0.1);
        }

        .header {
            text-align: center;
            padding-bottom: 20px;
        }

        .header h2 {
            color: #2c3e50;
        }

        .content {
            font-size: 15px;
            color: #333;
            line-height: 1.6;
        }

        .btn {
            display: inline-block;
            padding: 12px 18px;
            background: #e74c3c;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            margin-top: 15px;
        }

        .footer {
            margin-top: 20px;
            font-size: 12px;
            text-align: center;
            color: #888;
        }

        .note {
            font-size: 13px;
            color: #777;
            margin-top: 10px;
        }
    </style>
</head>

<body>

<div class="container">

    <div class="header">

        <img src="cid:Logo" style="width:50px; margin-bottom:10px;" />

        <h2>Password Reset Request</h2>
    </div>

    <div class="content">

        <p>Hello <b>${username}</b>,</p>

        <p>
            We received a request to reset your password for your <b>${company}</b> account.
        </p>

        <p>
            Click the button below to reset your password:
        </p>

        <p style="text-align:center;">
            <a class="btn" href="${link}">Reset Password</a>
        </p>

        <p class="note">
            This link will expire in <b>${expiry}</b> minutes.
        </p>

        <p class="note">
            If you did not request this, you can safely ignore this email.
        </p>

    </div>

    <div class="footer">
        © ${year} ${company}. All rights reserved.
    </div>

</div>

</body>
</html>
import { Route, Routes, Navigate, useNavigate } from "react-router-dom";
import "./App.css";
import ForgotPassword from "./component/auth/ForgotPassword";
import SignIn from "./component/auth/SignIn";
import SignUp from "./component/auth/SignUp";
import { useState } from "react";
import axios from "axios";
import Profile from "./component/profile/Profile";
import AddCourse from "./component/instructor/AddCourse";
import ManageCourse from "./component/instructor/manage-course/ManageCourse";
import InstructorDashboard from "./component/instructor/InstructorDashboard";
import Setup from "./component/instructor/manage-course/Setup";
import Film from "./component/instructor/manage-course/Film";
import CourseStructure from "./component/instructor/manage-course/CourseStructure";
import CourseDescription from "./component/instructor/manage-course/CourseDescription";
import Curriculum from "./component/instructor/manage-course/Curriculum";
import StudentDashboard from "./component/student/StudentDashboard";
import EditProfile from './component/profile/EditProfile';
import EditProfilePic from './component/profile/EditProfilePic';
import EditAccountSecurity from './component/profile/EditAccountSecurity';
import EditBankAccountDetails from './component/profile/EditBankAccountDetails';

function App() {

  
  // const userData1={
  //   "studentId": 4,
  //   "studentName": "Radhika",
  //   "studentEmail": "radhikashah1612@gmail.com",
  //   "studentPassword": "$2a$10$z1E9hcackg/ZezKViK0YseXf0GYqJ9744ur6ehSM6m9mICimkySvK"
  // }

  // const userData2={
  //   "instructorId": 1,
  //   "instructorName": "Radhika Shah",
  //   "instructorEmail": "radhikashah1612@gmail.com",
  //   "instructorPassword": "$2a$10$z1E9hcackg/ZezKViK0YseXf0GYqJ9744ur6ehSM6m9mICimkySvK",
  //   "bankIfscCode": "HDFC0001612",
  //   "accountNumber": 59183912839281
  // }

  const [loggedInStatus, setLoggedInStatus] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [userData, setUserData] = useState({});
  const navigate = useNavigate();

  const handleLogin = async () => {
    setIsLoading(true);
    const data = await axios
      .get("http://localhost:8080/greetings", {
        headers: {
          "Authorization": `Bearer ${localStorage.getItem("userToken")}`,
        },
      })
      .then((response) => setUserData(response.data))
      .catch((err) => console.log(err));
    setLoggedInStatus(true);
    setIsLoading(false);
  };
  const handleLogout = () => {
    localStorage.removeItem("userToken");
    localStorage.removeItem("userType");
    setUserData({});
    setLoggedInStatus(false);
    navigate("/");
  };

  return (
    <div className="App">
      <Routes>
        <Route
          path="/"
          exact
          element={
            loggedInStatus ? (
              localStorage.getItem("userType") === "student" ? (
                <Navigate replace to="/home" />
              ) : (
                <Navigate replace to="/instructor" />
              )
            ) : (
              <SignIn handleLogin={handleLogin} />
            )
          }
        />
        <Route path="/sign-up" element={<SignUp />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route
          path="/home"
          element={
            <StudentDashboard handleLogout={handleLogout} userData={userData} />
          }
        />
        {/* <Route path="/profile" element={<Profile />} /> */}
     
          <Route path="/profile" exact element={<Profile />}>
            <Route path="edit-profile" element={<EditProfile userData={userData}/>}></Route>
            <Route path="edit-profile-pic" element={<EditProfilePic userData={userData} />} />
            <Route
              path="edit-account-security"
              element={<EditAccountSecurity userData={userData}/>}
            />
            <Route
              path="edit-bank-account-details"
              element={<EditBankAccountDetails userData={userData}/>}
            />
            <Route path="*"/>
          </Route>
        <Route
          path="/instructor"
          element={
            <InstructorDashboard
              handleLogout={handleLogout}
              userData={userData}
            />
          }
        />
        <Route path="/instructor/add-course" element={<AddCourse />} />
        <Route
          path="/instructor/course/:courseId/manage"
          element={<ManageCourse />}
        >
          <Route path="course-structure" element={<CourseStructure />} />
          <Route path="setup" element={<Setup />} />
          <Route path="film" element={<Film />} />
          <Route path="description" element={<CourseDescription />} />
          <Route path="curriculum" element={<Curriculum />} />
        </Route>
      </Routes>
    </div>
  );
}

export default App;

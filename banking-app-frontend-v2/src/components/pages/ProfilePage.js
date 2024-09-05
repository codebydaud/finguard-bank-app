
import ProfileForm from "../ProfileForm";
import { useAuth } from '../../contexts/AuthContext'; 
import ProfileIllustration from "../ProfileIllustration";
export default function Signup() {
    const { currentUser } = useAuth();
  return (
    <>
     <h1>{currentUser.name}'s Profile</h1>

      <div className="column">
        <ProfileIllustration/>
        <ProfileForm />
      </div>
    </>
  );
}

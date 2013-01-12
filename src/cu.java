import java.io.DataInputStream;
import java.io.DataOutputStream;

public class cu extends ef
{
  public static int a = 119;
  public String b;
  private boolean c;

  public cu()
  {
    this.c = true;
  }

  public cu(String paramString)
  {
    this(paramString, true);
  }

  public cu(String paramString, boolean paramBoolean)
  {
    this.c = true;
    if (paramString.length() > a)
      paramString = paramString.substring(0, a);
    this.b = paramString;
    this.c = paramBoolean;
  }

  public void a(DataInputStream paramDataInputStream)
  {
    this.b = a(paramDataInputStream, a);
  }

  public void a(DataOutputStream paramDataOutputStream)
  {
    a(this.b, paramDataOutputStream);
  }

  public void a(eg parameg)
  {
    MacSpeech.post(this.b);
    parameg.a(this);
  }

  public int a()
  {
    return 2 + this.b.length() * 2;
  }

  public boolean d()
  {
    return this.c;
  }

  public boolean a_()
  {
    return !this.b.startsWith("/");
  }
}
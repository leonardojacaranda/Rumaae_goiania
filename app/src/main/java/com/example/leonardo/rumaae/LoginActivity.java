package com.example.leonardo.rumaae;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.NoSubscriberEvent;
import org.greenrobot.eventbus.Subscribe;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Mantem o controle da tarefa de login para garantir que possamos cancelá-lo se for solicitado.
     */
    private WebTaskLogin mAuthTask = null;

    // referencias UI.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //cria uma instância para o login daquele momento
        setContentView(R.layout.activity_login);
        // Configuraçao do fomulário de login
        mEmailView = (EditText) findViewById(R.id.email); //campo para digitar o email

        mPasswordView = (EditText) findViewById(R.id.password); // campo para digitar a senha
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) { //caso o email e a senha tenham sido validados, return TRUE
                    attemptLogin(null);
                    return true;
                }
                return false;
            }
        });


        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    public void onStart() {  //Usando a biblioteca EventBus, você pode passar mensagens de uma classe para uma ou mais
        super.onStart();     //classes em apenas algumas linhas de código. Além disso, todas as classes envolvidas são
        EventBus.getDefault().register(this);//completamente desacopladas uma da outra, levando a um codigo que é menos complexo e mais fácil de manter e depurar.
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     *As tentativas de login ou registre a conta especificada pelo formulário de login..
     * Se houver erros de formulário ( e-mail inválido , campos em falta , etc.), os
     *  erros são apresentados e nenhuma tentativa de login real é feito.
     */
    public void attemptLogin(View v) {
        if (mAuthTask != null) {
            return;
        }

        // Repor erros.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Armazenar valores no momento da tentativa de login.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Verificar se o usuário digitou uma senha valida.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Verifica se o usuário digitou um email valido.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            //Ouve um erro, tente tratá-lo.
            focusView.requestFocus();
        } else {
            // Mostrar um spinner progresso e lançar uma tarefa em
            //segundo plano para executar a tentativa de login do usuário .
            showProgress(true);
            mAuthTask = new WebTaskLogin(this,email, password);
            mAuthTask.execute();
        }
    }
    //Restrição para cada email ter '@'
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    //Restriçao para a senha ter no minimo 4 caracteres
    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * O tela de login desaparece para mostrar o progresso da autenticação
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // As APIs ViewPropertyAnimator não estão disponíveis ,
            // ocultar os componentes de interface do usuário relevantes.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Handle the EventBus User event.
     * @param user
     *  the user replied by any class.
     *  More information here: http://greenrobot.org/eventbus/documentation/how-to-get-started/
     */
    @Subscribe
    public void onEvent(User user){
        mAuthTask = null;
        showProgress(false);

        Intent activity = new Intent(this, Login.class);
        activity.putExtra("message","olá");
        startActivity(activity);

    }

    @Subscribe
    public void onEvent(Error error){
        mAuthTask = null;
        showProgress(false);

        Snackbar snackbar = Snackbar.make(mLoginFormView, error.getMessage(), Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Subscribe
    public void onAnything(NoSubscriberEvent randomEvent){
        mAuthTask = null;
        showProgress(false);
    }

}

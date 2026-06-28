import UIKit
import SwiftUI
import FirebaseCore
import GoogleSignIn
import Shared

struct ComposeView: UIViewControllerRepresentable {
    private let googleAuthRepository = GoogleAuthRepository()

    func makeUIViewController(context: Self.Context) -> UIViewController {
        MainViewControllerKt.MainViewController(
            onGoogleLoginClick: {
                signInWithGoogle()
            }
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Self.Context) {}

    private func signInWithGoogle() {
        guard let clientID = FirebaseApp.app()?.options.clientID else {
            print("Google sign-in failed: missing Firebase client ID")
            return
        }
        guard let presentingViewController = UIApplication.shared.topMostViewController else {
            print("Google sign-in failed: missing presenting view controller")
            return
        }

        GIDSignIn.sharedInstance.configuration = GIDConfiguration(clientID: clientID)
        GIDSignIn.sharedInstance.signIn(withPresenting: presentingViewController) { result, error in
            if let error {
                print("Google sign-in failed: \(error.localizedDescription)")
                return
            }
            guard let user = result?.user,
                  let idToken = user.idToken?.tokenString
            else {
                print("Google sign-in failed: missing token")
                return
            }

            let accessToken = user.accessToken.tokenString
            googleAuthRepository.signInWithGoogle(idToken: idToken, accessToken: accessToken) { user, error in
                if let error {
                    print("Firebase sign-in failed: \(error.localizedDescription)")
                    return
                }
                guard let user else {
                    print("Firebase sign-in failed: missing user")
                    return
                }

                print("Firebase sign-in succeeded: uid=\(user.uid), email=\(user.email ?? "nil")")
            }
        }
    }
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea()
    }
}

private extension UIApplication {
    var topMostViewController: UIViewController? {
        connectedScenes
            .compactMap { $0 as? UIWindowScene }
            .flatMap(\.windows)
            .first { $0.isKeyWindow }?
            .rootViewController?
            .topMostViewController
    }
}

private extension UIViewController {
    var topMostViewController: UIViewController {
        if let presentedViewController {
            return presentedViewController.topMostViewController
        }
        if let navigationController = self as? UINavigationController,
           let visibleViewController = navigationController.visibleViewController {
            return visibleViewController.topMostViewController
        }
        if let tabBarController = self as? UITabBarController,
           let selectedViewController = tabBarController.selectedViewController {
            return selectedViewController.topMostViewController
        }
        return self
    }
}

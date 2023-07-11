import cats.Monad


import cats.Monad
import cats.implicits._

package object utils {
  def transform[F[_] : Monad, A, B](fa: F[A])( f: A => B): F[B] =
    fa.flatMap(a => Monad[F].pure(f(a)))
}

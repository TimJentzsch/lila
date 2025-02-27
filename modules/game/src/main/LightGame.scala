package lila.game

import chess.{ Color, Status }

case class LightGame(
    id: GameId,
    whitePlayer: Player,
    blackPlayer: Player,
    status: Status
):
  def playable                                       = status < Status.Aborted
  def player(color: Color): Player                   = color.fold(whitePlayer, blackPlayer)
  def player(playerId: GamePlayerId): Option[Player] = players find (_.id == playerId)
  def players                                        = List(whitePlayer, blackPlayer)
  def playerByUserId(userId: UserId): Option[Player] = players.find(_.userId contains userId)
  def winner                                         = players find (_.wins)
  def wonBy(c: Color): Option[Boolean]               = winner.map(_.color == c)
  def finished                                       = status >= Status.Mate

object LightGame:

  import Game.{ BSONFields as F }

  def projection =
    lila.db.dsl.$doc(
      F.whitePlayer -> true,
      F.blackPlayer -> true,
      F.playerUids  -> true,
      F.winnerColor -> true,
      F.status      -> true
    )

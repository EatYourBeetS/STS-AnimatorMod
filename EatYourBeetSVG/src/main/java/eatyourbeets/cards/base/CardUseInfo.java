package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class CardUseInfo
{
    public final EYBCard Card;
    public final AbstractCard PreviousCard;
    public final EYBCardAffinities Synergies;
    public final ArrayList<AbstractMonster> Enemies;
    public final boolean CanActivateSemiLimited;
    public final boolean CanActivateLimited;
    public final boolean IsSynergizing;
    public final boolean IsStarter;

    public CardUseInfo(EYBCard card)
    {
        this.Card = card;
        this.PreviousCard = CombatStats.Affinities.GetLastCardPlayed();
        this.Synergies = CombatStats.Affinities.GetSynergies(card, PreviousCard);
        this.Enemies = GameUtilities.GetEnemies(true);
        this.CanActivateSemiLimited = CombatStats.CanActivateSemiLimited(card.cardID);
        this.CanActivateLimited = CombatStats.CanActivateLimited(card.cardID);
        this.IsSynergizing = CombatStats.Affinities.IsSynergizing(card);
        this.IsStarter = card.IsStarter();
    }

    public boolean TryActivateLimited()
    {
        return CombatStats.TryActivateLimited(Card.cardID);
    }

    public boolean TryActivateSemiLimited()
    {
        return CombatStats.TryActivateSemiLimited(Card.cardID);
    }

    public String GetPreviousCardID()
    {
        return PreviousCard != null ? PreviousCard.cardID : "";
    }
}

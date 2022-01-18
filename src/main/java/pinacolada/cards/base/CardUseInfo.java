package pinacolada.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLGameUtilities;

import java.util.ArrayList;

public class CardUseInfo
{
    public final PCLCard Card;
    public final AbstractCard PreviousCard;
    public final PCLCardAffinities Synergies;
    public final ArrayList<AbstractMonster> Enemies;
    public final boolean CanActivateSemiLimited;
    public final boolean CanActivateLimited;
    public final boolean IsSynergizing;
    public final boolean IsStarter;

    public CardUseInfo(PCLCard card)
    {
        this.Card = card;
        this.PreviousCard = PCLCombatStats.MatchingSystem.GetLastCardPlayed();
        this.Synergies = PCLCombatStats.MatchingSystem.GetSynergies(card, PreviousCard);
        this.Enemies = PCLGameUtilities.GetEnemies(true);
        this.CanActivateSemiLimited = CombatStats.CanActivateSemiLimited(card.cardID);
        this.CanActivateLimited = CombatStats.CanActivateLimited(card.cardID);
        this.IsSynergizing = PCLCombatStats.MatchingSystem.IsSynergizing(card);
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

package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class CardUseInfo
{
    public final EYBCard Card;
    public final AbstractMonster Target;
    public final AbstractCard PreviousCard;
    public final EYBCardAffinities Synergies;
    public final ArrayList<AbstractMonster> Enemies;
    public final boolean CanActivateSemiLimited;
    public final boolean CanActivateLimited;
    public final boolean IsStarter;
    public final boolean IsSynergizing = true;

    protected Object data;

    public CardUseInfo(EYBCard card, AbstractMonster target)
    {
        this.Card = card;
        this.Target = target;
        this.PreviousCard = CombatStats.Affinities.GetLastCardPlayed();
        this.Synergies = null;
        this.Enemies = GameUtilities.GetEnemies(true);
        this.CanActivateSemiLimited = CombatStats.CanActivateSemiLimited(card.cardID);
        this.CanActivateLimited = CombatStats.CanActivateLimited(card.cardID);
        this.IsStarter = CombatStats.CanActivatedStarter();
    }

    public boolean TryActivateStarter()
    {
        return CombatStats.TryActivateStarter();
    }

    public boolean TryActivateLimited()
    {
        return CombatStats.TryActivateLimited(Card.cardID);
    }

    public boolean TryActivateSemiLimited()
    {
        return CombatStats.TryActivateSemiLimited(Card.cardID);
    }

    public void SetTempData(Object data)
    {
        this.data = data;
    }

    public <T> T GetData(T defaultValue)
    {
        return data != null ? (T) data : defaultValue;
    }

    public boolean HasData()
    {
        return data != null;
    }
}

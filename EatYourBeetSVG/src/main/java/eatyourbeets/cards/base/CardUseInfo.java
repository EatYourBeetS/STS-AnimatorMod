package eatyourbeets.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class CardUseInfo
{
    public final AbstractCard Card;
    public final AbstractMonster Target;
    public final AbstractCard PreviousCard;
    public final ArrayList<AbstractMonster> Enemies;
    public final boolean CanActivateSemiLimited;
    public final boolean CanActivateLimited;
    public final boolean IsStarter;
    public final boolean IsSynergizing;

    protected Object data;

    public CardUseInfo(AbstractCard card, AbstractMonster target)
    {
        this.Card = card;
        this.Target = target;
        this.PreviousCard = CombatStats.Affinities.GetLastCardPlayed();
        this.Enemies = GameUtilities.GetEnemies(true);
        this.CanActivateSemiLimited = CombatStats.CanActivateSemiLimited(card.cardID);
        this.CanActivateLimited = CombatStats.CanActivateLimited(card.cardID);
        this.IsStarter = CombatStats.CanActivatedStarter();
        this.IsSynergizing = CardSeries.Synergy.WouldSynergize(card);
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

    public <T> T SetData(T data)
    {
        return (T)(this.data = data);
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

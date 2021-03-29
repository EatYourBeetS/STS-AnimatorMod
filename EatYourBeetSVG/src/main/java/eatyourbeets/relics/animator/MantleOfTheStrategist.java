package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnSynergyCheckSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.relics.AnimatorRelic;

public class MantleOfTheStrategist extends AnimatorRelic implements OnSynergyCheckSubscriber
{
    public static final String ID = CreateFullID(MantleOfTheStrategist.class);

    public MantleOfTheStrategist()
    {
        super(ID, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        CombatStats.onSynergyCheck.Unsubscribe(this);
        CombatStats.onSynergyCheck.Subscribe(this);
        SetEnabled(true);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m)
    {
        super.onPlayCard(c, m);

        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.size() == 2)
        {
            SetEnabled(false);
        }
    }

    @Override
    public boolean OnSynergyCheck(AbstractCard a, AbstractCard b)
    {
        return IsEnabled() && AbstractDungeon.actionManager.cardsPlayedThisTurn.size() > 0;
    }
}
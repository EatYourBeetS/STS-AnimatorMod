package eatyourbeets.cards.animator.series.TouhouProject;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class YoumuKonpaku extends AnimatorCard implements OnStartOfTurnSubscriber
{
    public static final EYBCardData DATA = Register(YoumuKonpaku.class)
            .SetAttack(1, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public YoumuKonpaku()
    {
        super(DATA);

        Initialize(5, 3);
        SetUpgrade(2, 1);

        SetAffinity_Green(1, 0, 1);
        SetAffinity_Red(1);

        SetInnate(true);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (enemy != null && enemy.currentHealth >= enemy.maxHealth)
        {
            amount *= 2;
        }

        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public void OnStartOfTurn()
    {
        if (player.discardPile.contains(this) && CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Top.MoveCard(this, player.discardPile, player.hand);
            GameActions.Bottom.Flash(this);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
        .SetDamageEffect(c -> GameEffects.List.Add(VFX.Clash(c.hb)).duration)
        .SetVFXColor(Color.TEAL);

        if (AgilityStance.IsActive())
        {
            CombatStats.onStartOfTurn.SubscribeOnce(this);
        }
    }
}
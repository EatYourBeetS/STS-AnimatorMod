package eatyourbeets.cards.animator.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.effects.attack.SmallLaser2Effect;
import eatyourbeets.utilities.GameActions;

public class Aisha extends AnimatorCard implements Spellcaster
{
    public static final String ID = Register(Aisha.class.getSimpleName(), EYBCardBadge.Special);

    public Aisha()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(2, 0, 1);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        return super.calculateModifiedCardDamage(player, mo, tmp + Spellcaster.GetScaling());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int max = p.filledOrbCount();
        for (int i = 0; i < max; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.FIRE)
            .SetOptions2(true, false)
            .SetDamageEffect(enemy -> AbstractDungeon.effectsQueue.add(new SmallLaser2Effect
            (AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY,
            enemy.hb.cX + MathUtils.random(-0.05F, 0.05F),
            enemy.hb.cY + MathUtils.random(-0.05F, 0.05F), Color.VIOLET)));
        }

        if (!EffectHistory.HasActivatedLimited(cardID))
        {
            if (p.filledOrbCount() == p.orbs.size())
            {
                GameActions.Bottom.GainOrbSlots(1);
                EffectHistory.TryActivateLimited(cardID);
            }
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
        }
    }
}
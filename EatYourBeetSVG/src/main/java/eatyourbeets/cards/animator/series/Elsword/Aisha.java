package eatyourbeets.cards.animator.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.attack.SmallLaser2Effect;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class Aisha extends AnimatorCard implements Spellcaster
{
    public static final EYBCardData DATA = Register(Aisha.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Elemental);

    public Aisha()
    {
        super(DATA);

        Initialize(2, 0, 0);
        SetUpgrade(0, 0, 1);
        SetScaling(1, 0, 0);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    protected void OnUpgrade()
    {
        upgradedDamage = true;
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        magicNumber = baseMagicNumber + player.filledOrbCount();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int max = p.filledOrbCount();
        for (int i = 0; i < max; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.FIRE)
            .SetOptions(true, false)
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
}
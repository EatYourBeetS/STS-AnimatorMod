package eatyourbeets.cards.animator.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.vfx.SmallLaserEffect;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.powers.common.IntellectPower;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

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
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.FIRE)
            .SetVFX(true, false)
            .SetDamageEffect(enemy -> GameEffects.Queue.Add(new SmallLaserEffect(player.hb.cX, player.hb.cY,
            enemy.hb.cX + MathUtils.random(-0.05f, 0.05f),enemy.hb.cY + MathUtils.random(-0.05f, 0.05f), Color.VIOLET)));
        }

        if (!EffectHistory.HasActivatedSemiLimited(cardID))
        {
            IntellectPower intellect = GameUtilities.GetPower(player, IntellectPower.class);
            if (intellect != null && intellect.GetCurrentLevel() > 1)
            {
                GameActions.Bottom.GainOrbSlots(1);
                EffectHistory.TryActivateSemiLimited(cardID);
            }
        }
    }
}
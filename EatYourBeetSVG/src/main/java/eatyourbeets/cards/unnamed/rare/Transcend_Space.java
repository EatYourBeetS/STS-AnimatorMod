package eatyourbeets.cards.unnamed.rare;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Transcend_Space extends UnnamedCard
{
    public static final int MAX_AMOUNT = 30;
    public static final EYBCardData DATA = Register(Transcend_Space.class)
            .SetMaxCopies(1)
            .SetAttack(2, CardRarity.RARE, EYBAttackType.Elemental, EYBCardTarget.ALL);

    public Transcend_Space()
    {
        super(DATA);

        Initialize(2, 0, 2, MAX_AMOUNT);
        SetUpgrade(0, 0, 1);

        SetEthereal(true);
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
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        for (EnemyIntent intent : GameUtilities.GetIntents())
        {
            final int amount = intent.GetDamage(false);
            if (amount > 0)
            {
                intent.AddWithering(Math.min(MAX_AMOUNT, amount));
            }
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.BorderLongFlash(Color.PURPLE);
        GameActions.Bottom.VFX(VFX.ShockWave(p.hb, Color.PURPLE), 0.7f).SetRealtime(true);

        for (int i = 0; i < magicNumber; i++)
        {
            final float pitch = Math.max(0.2f, 1 - (i * 0.3f));
            GameActions.Bottom.DealDamageToAll(this, AttackEffects.DARK)
            .SetSoundPitch(pitch, pitch);
        }

        for (AbstractMonster m1 : GameUtilities.GetEnemies(true))
        {
            final int amount = Math.min(MAX_AMOUNT, GameUtilities.GetIntent(m1).GetDamage(false));
            if (amount > 0)
            {
                GameActions.Bottom.StackAmplification(p, m1, amount);
                GameActions.Bottom.StackWithering(p, m1, amount);
            }
        }
    }
}
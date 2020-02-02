package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DaggerSprayEffect;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;

public class Viivi extends AnimatorCard
{
    public static final String ID = Register(Viivi.class);
    static
    {
        GetStaticData(ID).InitializePreview(ThrowingKnife.GetCardForPreview(), false);
    }

    public Viivi()
    {
        super(ID, 1, CardRarity.COMMON, EYBAttackType.Ranged, CardTarget.ALL);

        Initialize(3, 0, 3);
        SetUpgrade(0, 0, 1);
        SetScaling(0, 1, 0);

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().SetIconTag("???").AddMultiplier(magicNumber);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.CreateThrowingKnives(1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.VFX(new DaggerSprayEffect(AbstractDungeon.getMonsters().shouldFlipVfx()), 0.0F);
            GameActions.Bottom.DealDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.NONE);
        }
    }
}
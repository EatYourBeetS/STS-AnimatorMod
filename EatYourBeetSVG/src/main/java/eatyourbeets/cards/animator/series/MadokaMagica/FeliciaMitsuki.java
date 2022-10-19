package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.common.TemporaryDamageModifierPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class FeliciaMitsuki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(FeliciaMitsuki.class)
            .SetSkill(0, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public FeliciaMitsuki()
    {
        super(DATA);

        Initialize(0, 0, 50, 6);
        SetUpgrade(0, 0, 10, 0);

        SetAffinity_Red(1);

        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.Wait(GameEffects.List.ShowCopy(this, Settings.WIDTH * 0.75f, Settings.HEIGHT * 0.5f).duration * 0.35f);
        GameActions.Bottom.DealDamageToAll(DamageInfo.createDamageMatrix(secondaryValue, true, false), DamageInfo.DamageType.THORNS, AttackEffects.BLUNT_LIGHT);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new TemporaryDamageModifierPower(p, magicNumber).SetIcon(cardData.GetCardIcon()));
    }
}
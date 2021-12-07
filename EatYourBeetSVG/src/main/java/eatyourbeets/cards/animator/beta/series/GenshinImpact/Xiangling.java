package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.Xiangling_Guoba;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.common.BurningPower;
import eatyourbeets.powers.common.FreezingPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Xiangling extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Xiangling.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.ALL).SetSeriesFromClassPackage(true)
            .PostInitialize(data -> data.AddPreview(new Xiangling_Guoba(), false));

    public Xiangling()
    {
        super(DATA);

        Initialize(6, 0, 2, 0);
        SetUpgrade(3, 0, 0, 0);
        SetAffinity_Red(1, 0, 1);
        SetAffinity_Orange(0, 0, 1);
        SetAffinity_Green(0, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamageToAll(this, AttackEffects.SLASH_HORIZONTAL).forEach(d -> d.SetVFXColor(Color.FIREBRICK));

        for (AbstractMonster enemy : GameUtilities.GetEnemies(true)) {
            if (GameUtilities.GetPowerAmount(enemy, BurningPower.POWER_ID) > 0 || GameUtilities.GetPowerAmount(enemy, FreezingPower.POWER_ID) > 0) {
                GameActions.Bottom.GainTemporaryHP(magicNumber);
            }
        }

        if (info.IsSynergizing) {
            GameActions.Bottom.MakeCardInDrawPile(new Xiangling_Guoba());
        }
    }
}


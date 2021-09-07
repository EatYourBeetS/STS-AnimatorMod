package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.IchigoKurosaki_Bankai;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.affinity.ForcePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class IchigoKurosaki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(IchigoKurosaki.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Normal, EYBCardTarget.Random).SetSeriesFromClassPackage();
    static
    {
        DATA.AddPreview(new IchigoKurosaki_Bankai(), false);
    }

    public IchigoKurosaki()
    {
        super(DATA);

        Initialize(2, 0, 0, 6);
        SetUpgrade(3, 0, 0, 0);
        SetAffinity_Red(2, 0, 1);
        SetAffinity_Green(1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);

        GameActions.Bottom.GainForce(1, false);
        GameActions.Bottom.GainAgility(1, false);

        GameActions.Bottom.Callback(card -> {
            if (GameUtilities.GetPowerAmount(p, ForcePower.POWER_ID) > secondaryValue)
            {
                GameActions.Bottom.MakeCardInDrawPile(new IchigoKurosaki_Bankai());
                GameActions.Last.ModifyAllInstances(uuid).AddCallback(GameActions.Bottom::Exhaust);
            }
        });
    }
}
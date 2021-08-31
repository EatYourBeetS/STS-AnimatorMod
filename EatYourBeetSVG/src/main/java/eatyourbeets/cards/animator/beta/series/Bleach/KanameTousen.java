package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.status.Frostbite;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.powers.common.BlindedPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class KanameTousen extends AnimatorCard implements Hidden {
    public static final EYBCardData DATA = Register(KanameTousen.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.ALL).SetSeriesFromClassPackage();

    static {
        DATA.AddPreview(new Frostbite(), true);
    }

    public KanameTousen() {
        super(DATA);

        Initialize(0, 5, 2, 1);
        SetUpgrade(0, 0, 0);
        SetAffinity_Dark(1, 0, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Green(1, 0, 0);
        SetExhaust(true);

        SetAffinityRequirement(Affinity.Red, 3);
    }

    @Override
    protected void OnUpgrade() {
        SetHaste(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing) {
        GameActions.Bottom.GainBlock(block);
        for (AbstractMonster mo : GameUtilities.GetEnemies(true)) {
            if (mo.hasPower(BlindedPower.POWER_ID)) {
                GameActions.Bottom.GainTemporaryArtifact(secondaryValue);
            }
            else {
                GameActions.Bottom.ApplyBlinded(player, mo, magicNumber);
            }
        }

        if (CheckAffinity(Affinity.Red)) {
            //TODO
        }



    }

}
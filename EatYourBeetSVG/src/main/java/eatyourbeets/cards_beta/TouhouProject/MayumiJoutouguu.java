package eatyourbeets.cards_beta.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards_beta.special.MayumiJoutouguu_Haniwa;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;

public class MayumiJoutouguu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MayumiJoutouguu.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new MayumiJoutouguu_Haniwa(), true));

    public MayumiJoutouguu()
    {
        super(DATA);

        Initialize(0, 5, 2);
        SetUpgrade(0, 1, 1);

        SetAffinity_Red(1);
        SetAffinity_Green(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {

    }
}
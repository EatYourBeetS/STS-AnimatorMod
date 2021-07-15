package eatyourbeets.cards.animator.beta.series.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.Haniwa;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class MayumiJoutouguu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MayumiJoutouguu.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None).SetSeriesFromClassPackage();
    static
    {
        DATA.AddPreview(new Haniwa(), true);
    }

    public MayumiJoutouguu()
    {
        super(DATA);

        Initialize(0, 5, 2);
        SetUpgrade(0, 1, 1);

        SetCooldown(1, 0, this::OnCooldownCompleted);

        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);

        if (isSynergizing)
        {
            GameActions.Bottom.GainPlatedArmor(magicNumber);
        }
        cooldown.ProgressCooldownAndTrigger(m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        for (int i=0; i<2; i++)
        {
            GameActions.Bottom.MakeCardInDrawPile(new Haniwa());
        }
    }
}
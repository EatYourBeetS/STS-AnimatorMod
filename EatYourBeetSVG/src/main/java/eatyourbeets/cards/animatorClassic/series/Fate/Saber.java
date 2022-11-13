package eatyourbeets.cards.animatorClassic.series.Fate;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animatorClassic.special.Saber_Excalibur;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class Saber extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Saber.class).SetSeriesFromClassPackage().SetAttack(1, CardRarity.RARE);
    static
    {
        DATA.AddPreview(new Saber_Excalibur(), false);
    }

    public Saber()
    {
        super(DATA);

        Initialize(9, 0, 0);
        SetUpgrade(2, 0, 0);
        SetScaling(0, 1, 1);

        SetCooldown(8, 0, this::OnCooldownCompleted);
        SetLoyal(true);
        
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);

        int progress = 1;
        if (info.IsSynergizing)
        {
            progress += 2;
        }

        cooldown.ProgressCooldownAndTrigger(progress, m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.Purge(uuid);
        GameActions.Bottom.MakeCardInHand(new Saber_Excalibur());
    }
}
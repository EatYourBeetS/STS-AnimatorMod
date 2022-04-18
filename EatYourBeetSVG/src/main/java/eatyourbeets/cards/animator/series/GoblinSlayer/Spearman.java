package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.status.Status_Wound;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Spearman extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Spearman.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Piercing)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new Witch(), true);
                data.AddPreview(new Status_Wound(), false);
            });

    private static final CardEffectChoice choices = new CardEffectChoice();

    public Spearman()
    {
        super(DATA);

        Initialize(9, 0);
        SetUpgrade(3, 0);

        SetAffinity_Red(1);
        SetAffinity_Green(1);

        SetCardPreview(Witch.DATA::IsCard);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        cardPreview.enabled = HasSynergy();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SPEAR).SetVFXColor(Color.LIGHT_GRAY).SetSoundPitch(0.75f, 0.85f);
        GameActions.Bottom.GainAgility(1, true);
        GameActions.Bottom.GainForce(1, true);
        GameActions.Bottom.MakeCardInHand(new Status_Wound());

        if (info.IsSynergizing)
        {
            GameActions.Bottom.Draw(1)
            .SetFilter(Witch.DATA::IsCard, false)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    GameUtilities.Retain(c);
                }
            });
        }
    }
}
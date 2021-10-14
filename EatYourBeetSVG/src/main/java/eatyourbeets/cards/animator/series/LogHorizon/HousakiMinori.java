package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.animator.EnchantedArmorPower;
import eatyourbeets.utilities.GameActions;

public class HousakiMinori extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HousakiMinori.class)
            .SetSkill(1, CardRarity.COMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new HousakiTohya(), false));

    public HousakiMinori()
    {
        super(DATA);

        Initialize(0, 6, 15, 2);
        SetUpgrade(0, 1, 0);

        SetAffinity_Blue(1);
        SetAffinity_Light(1, 1, 1);

        SetCooldown(4, -1, this::OnCooldownCompleted);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        cooldown.ProgressCooldownAndTrigger(info.IsSynergizing && info.GetPreviousCardID().equals(HousakiTohya.DATA.ID) ? 3 : 1, m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new EnchantedArmorPower(player, magicNumber));
    }
}